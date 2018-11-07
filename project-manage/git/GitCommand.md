# <a name="top">Git常用命令</a>



-----

+ 每次 `git push` 都要输入账号和密码

  ```shell
  git remote rm origin

  git remote add origin git@github.com:HurricanGod/home.git
  ```




+  查看配置

   ```shell
   git config --list
   ```



+ 查看远程地址

  ```shell
  git remote -v
  ```

  ​


+ 对某个**未加入缓存区**文件放弃本地修改

  ```shell
  # 未进行 git add filename 的本地修改（即未加入缓存区的修改）可以使用以下命令放弃本地修改
  git checkout -- filename

  # "." 表示放弃本地所有修改（未加入缓存区的修改）
  git checkout .
  ```

  ​


+ 放弃**已加入缓存区但未提交**文件的修改

  ```shell
  git reset HEAD filename

  # 放弃未提交的缓存区的所有修改
  git reset HEAD .
  ```

  ​


+ 回退到某个版本（通常使用`git commit`提交了代码）

  ```shell
  # commit_id 是指每次执行 git commit 后产生的id
  # 可以使用 git log 查看 commit_id
  git reset --hard commit_id

  # 回退到上一次 commit 的状态
  git reset --hard HEAD^
  ```

  ![commit_id](https://github.com/HurricanGod/Home/blob/master/project-manage/git/img/commit_id.png)

  ​

  **背景** ：

  > ​     有1个文件在某次提交时被删除或者清空了，但一时没有注意到。经过n多次commit后才发现这个文件内容没了，此时我要多么希望能回到过去把文件复制出来，当前版本可以不用做任何改动，只需再`commit`一次就好了！
  >
  > ​    在 git 的世界里还真可以穿越，哈哈，没想到吧~
  >
  > ​

  ```shell
  # 步骤1：使用下面其中一个命令查看 commit  记录
  ① git log
  ② git log -- 待穿越的文件名

  # 重点：
  # 记下最新一个 commit 的 id，能不能穿越回来就靠它了
  # 假设当前最新的一个 commit_id 为 c3fbadcb7d28b37da3807a4e8981063cefe82fa7
  # 找到要恢复到的 commit_id，至于怎么找就看平时 commit 时规不规范
  # 假设要恢复的 commit_id 为 13d99a259725fdb244c4d210ba78d93b53261dde

  # 使用 git reset --hard commit id 回到过去
  git reset --hard 13d99a259725fdb244c4d210ba78d93b53261dde

  # 好了，回到过去了！把要恢复的文件单独复制出来
  # 如果穿越到过去不回来的话，穿越到的时期到当前最新一次commit时间内的修改都会丢失
  # 醒醒，回到现实吧
  git reset --hard c3fbadcb7d28b37da3807a4e8981063cefe82fa7

  ```

  ​

---

## 调整 Tab 字符所代表的空格数

+ 在URL后面添加 `?ts=4`




----


## 查看用户的所有Commit

+ 在URL后面添加 `?author={user}`






-----

## 整行高亮

+ 在**代码文件**后面添加`#L{start}`或`#L{start}-{end}`

<a href="https://github.com/cssmagic/blog/issues/49">正确接收Github信息邮件的方式</a>
