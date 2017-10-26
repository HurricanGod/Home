import ctypes
import datetime
import traceback
import time
import xlrd
import pymysql
import os

from multiprocessing import Queue
from multiprocessing import Process
from multiprocessing import Value


class Mysql(object):
    __slots__ = ()
    host = "localhost"
    port = 3306
    user = "root"
    password = "qwer123456"
    db = "pythondb"
    charset = "utf8"

    def __init__(self):
        pass

    @staticmethod
    def addToDb(sql, params):
        """
       往数据库插入一条数据
       :param sql: sql语句
       :param params: sql语句中的参数对应的值，params为元组类型
       :return: 受影响的行
       """
        connect = pymysql.connect(host=Mysql.host, port=Mysql.port, user=Mysql.user,
                                  passwd=Mysql.password, db=Mysql.db, charset=Mysql.charset)
        cur = connect.cursor()
        affect_line = 0
        try:
            cur.execute(sql, params)
            connect.commit()
            affect_line = cur.rowcount
        except Exception as e:
            print("sql执行异常：{e}".format(e=e))
            connect.rollback()
        else:
            connect.close()
            return affect_line


class ExcelHelper(object):
    def __init__(self, absolutepathname: str):
        self.excelname = absolutepathname  # excel文件的绝对路径名称
        self.sheets = self.loadExcelFile()

    def readExcelTableNames(self) -> list:
        """
        读取excel文件中含有的表名称
        :return: 列表类型，内容为excel中的表名
        """
        return self.sheets.sheet_names()

    def loadExcelFile(self):
        """
        加载excel文件的内容，如果输入的文件名不存在
        则会要求重新输入，直到输入文件存在为止
        :return:
        """
        if os.path.exists(self.excelname):
            return xlrd.open_workbook(self.excelname)
        else:
            self.excelname = input("刚才输入的excel文件不存在！\n请重新输入excel文件的绝对路径名:\t")
            self.loadExcelFile()

    def getColumnsNameFromTable(self, sheet_name: str) -> list:
        """
        获取excel表中某个表的所有列名
        :param sheet_name: 表名
        :return: list列表，每个元素表示表的一个列名
        """
        table = self.sheets.sheet_by_name(sheet_name)
        columns = table.ncols
        columns_name = []
        for i in range(0, columns):
            val = table.cell(0, i)
            columns_name.append(val)
        return columns_name

    def getTableRows(self, sheet_name: str) -> int:
        """
        获取excel表中含有多少行数据
        :param sheet_name: 表名
        :return: 整数，表中含有的数据条数
        """
        return self.getSheetByName(sheet_name).nrows - 1

    def getSheetByName(self, sheet_name):
        """
        根据 excel 表的表名获取表格对象
        :param sheet_name:
        :return:
        """
        return self.sheets.sheet_by_name(sheet_name)

    def getSheetByIndex(self, index):
        """
        根据索引获取 excel 表的表名获取表格对象
        :param index:
        :return:
        """
        table_names = self.readExcelTableNames()
        if index > len(table_names):
            raise Exception("索引越界异常, {} < {}".format(len(table_names), index))
        return self.sheets.sheet_by_index(index)

    def getSheetTablesInfo(self):
        tablemap = dict()
        for i in range(0, len(self.readExcelTableNames())):
            print(self.getSheetByIndex(i).name)
            tablemap[i] = self.getSheetByIndex(i).name
        return tablemap


class ScheduleMonitor(Process):
    """
    数据转储到Mysql的进度报告进程
    """
    def __init__(self, finishNumber: Value, total: int):
        super().__init__()
        self.finishNumber = finishNumber  # 共享内存变量，表示已经转储到数据库的数据条数
        self.total = total  #   要转储到Mysql数据库的总量

    def run(self):
        print("进度查看进程启动， pid = {}".format(self.pid))
        while True:
            time.sleep(3)
            current = self.finishNumber.value
            print("当前进度：\t{}".format(current / self.total))


class SaverProcess(Process):
    '''
    将数据保存到Mysql的进程
    '''
    def __init__(self, makerFinish: Value, tasks: Queue, sql: str):
        super().__init__()
        self.makerIsFinish = makerFinish  # 生产者停止生产标志，共享内存变量，boolean类型的包装类
        self.tasks = tasks  # 任务队列
        self.sqltempalte = sql  # sql语句模版

    def run(self):
        print("Saver Process is start, pid = {}".format(self.pid))
        while True:
            if self.makerIsFinish.value:
                break
            else:
                while not self.tasks.empty():
                    Mysql.addToDb(self.sqltempalte, self.tasks.get())


if __name__ == '__main__':
    print("将excel文件数据存到Mysql数据库，请先在数据库建好表，本程序只支持以下格式的插入语句")
    print("\tinsert into tablename values(值1, 值2)\n")
    print("不支持诸如insert into tablename(列名1, 列名2) values(值1, 值2)格式的sql语句")
    print("例：\n若执行insert into tablename values(值1, 值2)语句")
    print("则应该输入的sql模版语句为：insert into tablename values(%s, %s)")
    host = input("输入数据库所在服务器地址，本机数据库请输入 localhost：\t")
    db = input("输入要连接的Mysql数据库名:\t")
    user = input("输入数据库帐号:\t")
    pwd = input("输入数据库密码:\t")
    Mysql.host = host
    Mysql.db = db
    Mysql.user = user
    Mysql.password = pwd

    filename = input("输入excel文件绝对路径名称:\t")
    try:
        # "C:/Users/NewObject/Downloads/data.xlsx"
        # sql = "insert into dbdate values(%s, %s, %s, %s)"
        excelsheets = ExcelHelper(filename)
        table_names = excelsheets.readExcelTableNames()
        print("excel文件中的表名有：\n{}".format(table_names))
        sheet_name = input("输入你要操作的excel表名：\t")
        table = excelsheets.getSheetByName(sheet_name)
        nrows = table.nrows - 1
        ncols = table.ncols
        print("{} 表中还有 {} 条数据".format(sheet_name, nrows))
        sqltemplate = input("输入sql模版语句，要插入的值用 '%s'代替，请保持列顺序与数据库表中的列顺序一致\n")

        # 共享内存整型变量，用于记录excel表中已经到数据库条数
        finishNumber = Value(ctypes.c_int32, 0)

        # 共享内存bool变量，标志主进程是否通知生产数据
        makerIsFinish = Value(ctypes.c_bool, False)

        monitorProcess = ScheduleMonitor(finishNumber, nrows)

        # 把数据保存进度监测进程设置为守护进程，主进程退出时自动退出
        monitorProcess.daemon = True
        monitorProcess.start()

        # 初始化任务队列大小
        taskqueue = Queue(50)

        processNumber = int(input("您想使用几个进程来完成此任务?请输入一个大于1的整数:\t"))
        processList = []

        start = datetime.datetime.now()

        # 启动数据保存进程
        for i in range(0, processNumber):
            p = SaverProcess(makerIsFinish, taskqueue, sqltemplate)
            p.daemon = True
            processList.append(p)
            p.start()

        for index in range(1, nrows):
            paramlist = []
            for col in range(0, ncols):
                paramlist.append(table.cell_value(index, col))
            # 阻塞地往任务队列里添加数据
            taskqueue.put(tuple(paramlist))
            finishNumber.value = finishNumber.value + 1
            if index == 10000:
                break
        makerIsFinish.value = True
        end = datetime.datetime.now()
        print("{} 条数据从 excel 保存到 Mysql 花费的时间为：{}".format(finishNumber.value, end - start))
    except Exception as e:
        print("出现异常：\n{}\n".format(traceback.format_exc()))
        print("程序异常退出！")
        exit()
    print("程序正常结束！")
