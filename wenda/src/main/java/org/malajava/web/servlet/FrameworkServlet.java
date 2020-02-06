package org.malajava.web.servlet;


import org.malajava.web.context.RequestHolder;
import org.malajava.web.context.ResponseHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

// 不使用 public 修饰符，仅限 当前包内使用
abstract  class FrameworkServlet extends HttpServlet {

    /** 可以在 web.xml 中配置  context-param 初始化参数来指定 由容器所提供的 default servlet 的名称 */
    protected static final String DEFAULT_SERVLET_NAME_PARAM = "defaultServletName" ;

    /** 确定用来设置 控制器所在 包 的那个初始化参数的名称 */
    protected final String CONTROLLER_PACKAGE_NAME_PARAM = "controllerPackage" ;

    protected static final String DELIMITER_SLASH = "/" ;
    protected static final String DELIMITER_CLN = ":" ;

    /**   (\/\/w+){2,} 表示 /path 的形式至少需要出现两次，比如 /hello/world/test */
    protected static final String PATH_PATTERN = "(\\/\\w+){2,}";

    /** 用于保存当前 ServletContext 的引用  */
    protected ServletContext application ;

    /** 声明用来保存当前 Web 应用路径的属性 */
    protected String contextPath ;

    /** 专门声明一个用来存放 控制器类 的 Map 集合 ( 使用 RequestMapping 注解中 path 属性的值做 key , 控制器类做 value )*/
    protected final Map<String,Class<?>> controllerClassMap = new HashMap<>();;

    // 外部 Map 以 控制器类 上 RequestMapping 注解中 path 属性的值做 key
    // 内部 Map 以 控制器类中的方法 上 RequestMapping 注解中 path 属性的值做 key
    /** 专门声明一个用来为每个类存放所有方法的 Map 集合 ( 所有标注了 RequestMapping 注解的方法 ) */
    protected final Map<String,Map<String, Method>> methodMaps = new HashMap<>();;

    /** 保存容器所提供的 default servlet 的名称 */
    protected String defaultServletName ;

    /** 用来扫描所有的控制器类 */
    protected abstract List< Class<?> >  scanControllerClass();

    /** 检查给定的 List 中的所有的类，筛选那些标注了 Controller 注解的类，并予以处理 */
    protected abstract  void prepareControllerClass( List<Class<?>> list );

    /** 初始化 default servlet 的名称 */
    protected abstract void initDefaultServletName() ;

    /** 调用 由 容器所提供的 default servlet  */
    protected abstract void invokeDefaultServlet( HttpServletRequest request , HttpServletResponse response  )
            throws ServletException, IOException ;

    /** 清理会话对象中的 RedirectAttributes 对象 */
    protected abstract void  clearRedirectAttributes();

    /** 根据指定的控制器类型创建控制器实例 */
    protected abstract Object createControllerObject(Class<?> c );

    /**
     * 根据被执行方法的参数个数和类型来判断需要传入哪些实参，并通过 invokeMethod 来执行目标方法
     * @param controller 控制器类的实例
     * @param method 被执行的目标方法
     * @return  返回目标方法执行后的返回值
     * @throws RuntimeException 当参数个数或参数类型不符合约定要求时抛出 RuntimeException
     */
    protected  abstract Object process(Object controller , Method method ) throws RuntimeException ;

    protected abstract void handle( Method method , Object result ) throws ServletException, IOException;

    /**  专门用来从 uri 中获取本次所请求的资源路径  */
    protected abstract String extractURI() ;

    /** 从请求路径中抽取 与 控制器类 上标注的 RequestMapping 注解的 path 值相对应的一部分 */
    protected abstract String extractControllerPath();

    /** 从路径中抽取与 @RequestMapping 中 path 属性对应的那部分内容  */
    protected abstract String extractMethodPath() ;

    private Class<?> getControllerClass( String controllerPath ){
        Class<?> clazz = controllerClassMap.get( controllerPath ) ;
        if( clazz == null ) {
            throw new RuntimeException("未找到标有 @Controller( path = \"/" + controllerPath + "\" ) 的控制器类");
        }
        return clazz ;
    }

    private Method getControllerMethod( Class<?> controllerClass , String controllerPath , String methodPath ) {
        Map<String,Method> map = methodMaps.get( controllerPath );
        if( map == null || map.isEmpty() ){
            throw new RuntimeException( "在 " + controllerClass.getCanonicalName() + " 类中不存任何标注了 @RequestMapping 的方法");
        }

        Method method = map.get( methodPath );
        if( method == null ){
            throw  new RuntimeException( "在 " + controllerClass.getCanonicalName() + " 类中未找到标有 @RequestMapping( path = \"" + methodPath + "\" ) 的方法" );
        }
        return method ;
    }

    @Override
    public final void init() {
        // 初始化实例变量
        // 获取当前 ServletContext 对象 ( 即 JSP 内置对象中的 application 对象 )
        application = this.getServletContext();
        // 在初始化时就明确当前 Web 应用路径
        contextPath = application.getContextPath() ;

        // 初始化 default servlet 的名称
        this.initDefaultServletName();

        // 从指定包中加载所有类
        List< Class<?> > controllerClassList = this.scanControllerClass();
        // 处理所有标注了 @Controller 的类，并在方法内部处理每个类中标注了 @RequestMapping 的方法
        this.prepareControllerClass( controllerClassList );
    }

    /***
     * 重写 HttpServlet 类中的 service(HttpServletRequest , HttpServletResponse ) 方法，
     * 并在重写后的方法中实现根据请求路径( request uri ) 来调度请求。
     */
    protected final void service(HttpServletRequest request , HttpServletResponse response )
            throws ServletException, IOException {

        RequestHolder.setCurrentRequest( request ); // 将请求对象绑定到当前线程 ( 添加到线程局部变量中 )
        ResponseHolder.setCurrentResponse( response ); // 将响应对象绑定到当前线程 ( 添加到线程局部变量中 )

        /** 获取 request uri 并从中剔除 contextPath 部分并提取剩余部分 **/
        String uri = this.extractURI() ;

        // 判断请求路径是否符合指定的模式
        if( Pattern.matches( PATH_PATTERN , uri ) ) {
            String controllerPath = this.extractControllerPath() ;
            // 从 controllerClassMap 中获取指定路径对应的 Class 对象
            Class<?> controllerClass = this.getControllerClass( controllerPath ) ;
            // 如果 当前会话对象 中存在 RedirectAttributes ，则将其清除
            this.clearRedirectAttributes();
            // 从 请求路径 中抽取 与 控制器方法 的 RequestMapping 注解 path 属性相对应的部分
            String path = this.extractMethodPath();
            // 根据请求路径寻找相应的方法
            Method controllerMethod = this.getControllerMethod( controllerClass , controllerPath , path );
            Object controller = this.createControllerObject( controllerClass ) ; // 创建控制器类的实例
            Object result = this.process( controller , controllerMethod );
            this.handle( controllerMethod , result );
        } else {
            // 调用由容器提供的 default servlet 来处理
            this.invokeDefaultServlet( request , response );
        }

        RequestHolder.removeCurrentRequest(); // 将请求对象从当前线程中移除 ( 从线程局部变量中移除 )
        ResponseHolder.removeCurrentResponse(); // 将响应对象从当前线程中移除 ( 从线程局部变量中移除 )

    }

}
