## 文件上传

----

**SpringMVC**实现文件上传需要添加两个依赖的jar包：

+ `commons.fileupload.jar`
+ `commons.io.jar`



**Maven方式添加依赖**：

```xml

    <!-- 使用文件上传依赖的jar包-->
    <!-- https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload -->
    <dependency>
      <groupId>commons-fileupload</groupId>
      <artifactId>commons-fileupload</artifactId>
      <version>1.3.1</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
    <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>1.3.2</version>
    </dependency>

```



----

使用`ajax` **异步上传文件**

+ **依赖的js库** ——`ajaxfileupload.js`
+ 处理上传文件**控制器里的方法**需要`MultipartFile`类型的参数用于接收文件，如果**上传多个文件**需要用`MultipartFile[]`类型的参数接收，并用`@RequestParam`注解进行校正参数；
+ 在**Spring**配置文件里配置`CommonsMultipartResolver`对象，并把该对象**id**设置为
  `multipartResolver`（**id只能是multipartResolver**）
+ 开启**mvc**注解驱动

---

<a name="back">**示例Demo**</a>

+ <a href="#spring">**Spring.xml** 配置文件</a>
+ <a href="#web">**web.xml** 配置文件</a>
+ <a href="#ajaxupload">**前端ajax上传文件代码** </a>
+ <a href="#controller">**Controller里的处理逻辑** </a>


----

![](https://github.com/HurricanGod/Home/blob/master/spring-mvc/img/ajax-upload-1.png)

![](https://github.com/HurricanGod/Home/blob/master/spring-mvc/img/ajax-upload-2.png)

![](https://github.com/HurricanGod/Home/blob/master/spring-mvc/img/ajax-upload-3.png)

![](https://github.com/HurricanGod/Home/blob/master/spring-mvc/img/ajax-upload-4.png)



<a name="controller">**Controller里的处理逻辑** </a>

```java
@Controller
@RequestMapping(value = "/file")
public class FileUploadController {

    @RequestMapping(value = "/upload.do")
    @ResponseBody
    public Object handlerFileUpload(@RequestParam("filename") MultipartFile fileField,
                                    HttpServletRequest request) throws IOException {
        HashMap<String, Object> map = new HashMap<>();

        String path = request.getSession().getServletContext().getRealPath("/img");
        String[] extensions = { "gif", "jpg", "png","jpeg" };
        String filename = fileField.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();

        System.out.println("path = " + path);
        System.out.println("filename = " + filename);
        System.out.println("extName = " + extName);

        if (fileField.getSize() > 0) {

            /**
             *  判断上传的图片格式是否符合要求
             */
            boolean isLegal = false;
            for (int i = 0; i < extensions.length; i++) {
                if (extName.equals(extensions[i])) {
                    isLegal = true;
                    break;
                }
            }

            if (isLegal) {
                // 图片符合格式要求保存下来
                File file = new File(path, filename);
                fileField.transferTo(file);

                map.put("status", true);
                map.put("msg", "上传成功");
            } else {
                map.put("status", false);
                map.put("msg", "请不要上传gif、jpg、png、jpeg以外格式的图片");
            }

        }
        return map;
    }

}
```

<a href="#back">**back**</a>

<a name="ajaxupload">**前端ajax上传文件代码** </a>

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册页面</title>
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.net/Public/js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.net/Public/js/easyui/themes/icon.css">
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/ajaxfileupload.js"></script>

</head>
<body>

<div style="width:430px;background:#fafafa;padding:10px;">
    <div style="padding:3px 2px;border-bottom:1px solid #ccc">上传头像</div>
    <table>
        <tr>
            <td >文件名：</td>
            <td><input id="filename" name="filename" type="file"></td>
        </tr>

        <tr>
            <td></td>
            <td id="res" style="color: #ac2925" ></td>
        </tr>


        <tr>
            <td></td>
            <td><input type="button" value="上传" id="push"></td>
        </tr>
    </table>
</div>

<script type="text/javascript">
    $("#push").click(function(event){
        event.preventDefault();
        $("#res").text("");
        var isLegal = checkFileSize("#filename", 1024 * 1024 * 2);
        if(!isLegal){
            $("#res").text("上传的文件超过最大限度2M");
            return;
        }
        $.ajaxFileUpload({
            //用于文件上传的服务器端请求地址
            url: 'file/upload.do',

            //是否需要安全协议，一般设置为false
            secureuri: false,

            //文件上传域的ID
            fileElementId: 'filename',

            //返回值类型 一般设置为json
            dataType: 'json',

            //服务器成功响应处理函数
            success: function (data, status) {
                if(data.status == true){
                    $("#filename").val("");
                }
                $("#res").text(data.msg);
            }
        })

    })

    function checkFileSize(id, maxSize){
        var  browserCfg = {};
        var ua = window.navigator.userAgent;
        if (ua.indexOf("MSIE")>=1){
            browserCfg.ie = true;
        }else if(ua.indexOf("Firefox")>=1){
            browserCfg.firefox = true;
        }else if(ua.indexOf("Chrome")>=1){
            browserCfg.chrome = true;
        }

        var obj_file = $(id);
        if(obj_file.value==""){
            alert("请先选择上传文件");
            return false;
        }
        var size = obj_file[0].files[0].size;
        if(size < maxSize){
            return true;
        } else {
            return false;
        }
    }
</script>

</body>
</html>
```

<a href="#back">**back**</a>

<a name="spring">**Spring.xml** 配置文件</a>

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- SpringMVC默认异常处理器-->
    <!--<bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">-->
        <!--<property name="defaultErrorView" value="/page/error.html"/>-->
    <!--</bean>-->

    <!-- SpringMVC自定义异常处理器-->
    <!--<bean class="cn.hurrican.exceptions.AccountManageExceptionResolver" />-->

    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="defaultEncoding" value="utf-8"/>
        <property name="maxUploadSize" value="2097152"/>
    </bean>

    <context:component-scan base-package="cn.hurrican.*"/>

    <mvc:annotation-driven />

    <mvc:default-servlet-handler />

</beans>
```

<a href="#back">**back**</a>