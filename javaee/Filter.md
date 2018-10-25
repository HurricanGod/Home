## Filter

Filter必须实现javax.servlet.Filter接口

javax.servlet.Filter接口有三个方法

1. init( FilterConfig  config)
2. doFilter(ServletRequest request, ServletReaponse response,FilterChain chain)
3. destory()

web程序启动时会调用init()方法初始化Filter，并从参数config获取初始化参数及ServletContext信息，doFilter()方法每次有客户端请求时都会被调用一次，参数chain为滤镜链，通过chain.doFilter(request, response)将请求传给下一个Filter或Servlet。web程序卸载时会调用destory()方法

**注意**

过滤器里的doFilter方法一定要执行chain.doFilter(request,response)，否则request不会交给后面的Filter或者Servlet.
