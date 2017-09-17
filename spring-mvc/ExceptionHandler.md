## 异常处理

---

**常用的SpringMVC异常处理方式** ：

+ <a href="#typical">**系统自定义的异常处理器** </a>`SimpleMappingExceptionResolver`
+ <a href="#custom">**自定义异常处理器**</a>
+ <a href="#exceptionAnnotation">**异常处理注解**</a>


----

<a name="typical">**系统自定义的异常处理器** </a>`SimpleMappingExceptionResolver`

使用方法：在SpringMVC配置文件中注册该异常处理器bean，注册时不需要id属性，当异常发生时自动执行；当需要为每个**特定的异常**指定**要跳转的页面**时可以在`exceptionMappings` 里进行配置，`exceptionMappings`

的类型为`Properties`

```xml

    <bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
        <property name="defaultErrorView" value="/page/error.html"/>
    </bean>

```



-----

<a name="custom">**自定义异常处理器**</a>

自定义异常处理器需要实现`HandlerExceptionResolver`接口，并且需要**把自定义类注册到SpringMVC配置文件**里；实现了`HandlerExceptionResolver`接口的类，只要发生异常，都会自动执行接口方法`resolveException()`



**示例代码**：

+ 实现**HandlerExceptionResolver**接口

  ```java
  public class AccountManageExceptionResolver implements HandlerExceptionResolver {
      @Override
      public ModelAndView resolveException(HttpServletRequest request,
                                           HttpServletResponse response,
                                           Object handler, Exception ex) {
          ModelAndView mv = new ModelAndView();
          if (ex instanceof DateFormatException) {
              mv.setViewName("/page/error.html");
          }
          return mv;
      }
  }
  ```

+ 在Spring容器里配置**自定义异常处理器**

  ```xml
  <bean class="cn.hurrican.exceptions.AccountManageExceptionResolver" />
  ```

+ `Controller`里的方法使用**自定义异常处理器**

  ```java
  @Controller
  @RequestMapping(value = "/account/manage")
  public class AccountManageController {

      @RequestMapping(value = "/remember.do")
      public void rememberAccount(HttpServletResponse response, AccountMsgTransferData msg)
              throws DateFormatException {
          String time = msg.getRegisterTime();
          if (!DateTimeFormatUtil.checkDateTimeFormat(time)) {
              throw new DateFormatException("没有找到匹配的日期格式");
          }
      }
  }
  ```

  ​




上面代码中若前台传递的日期格式不对，将会抛出`DateFormatException`异常，**抛出异常**后将会转到`AccountManageExceptionResolver`类里去执行`resolveException()`方法，该方法把返回1个错误页面



---

<a name="exceptionAnnotation">**异常处理注解**</a>

要使用**异常处理注解** 需要在方法名上添加注解`@ExceptionHandler`，添加此注解后在`value`属性里指定**异常类**，即发生指定异常时将会执行`@ExceptionHandler`注解下的方法

```java
@Controller
@RequestMapping(value = "/account/manage")
public class AccountManageController {

    @RequestMapping(value = "/remember.do")
    public void rememberAccount(HttpServletResponse response, AccountMsgTransferData msg)
            throws DateFormatException {
        String time = msg.getRegisterTime();
        if (!DateTimeFormatUtil.checkDateTimeFormat(time)) {
            throw new DateFormatException("没有找到匹配的日期格式");
        }


    }


    @ExceptionHandler(value = DateFormatException.class)
    public void handlerDateFormatException(HttpServletResponse response){
        if (response != null) {
            PrintWriter writer = null;
            try {
                writer = response.getWriter();

                ServerTip tip = new ServerTip();
                tip.setMsg("填写的日期格式错误，正确格式为yyyy-MM-dd");

                JSONObject jsonObject = JSONObject.fromObject(tip);
                String s = jsonObject.toString();
                writer.write(s);

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }
}
```



![]()