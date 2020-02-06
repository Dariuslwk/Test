package org.malajava.web.servlet;

import com.alibaba.fastjson.JSON;
import org.malajava.foundation.ClassPathResourceLoader;
import org.malajava.web.context.ParameterHolder;
import org.malajava.web.context.RequestContext;
import org.malajava.web.context.RequestHolder;
import org.malajava.web.context.ResponseHolder;
import org.malajava.web.support.Controller;
import org.malajava.web.support.RedirectAttributes;
import org.malajava.web.support.RequestMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class DispatcherServlet extends FrameworkServlet {

    /** 指定 在 会话对象 中所存储的 RedirectAttributes 的名称 */
    private final String redirectAttributesName = this.getClass().getCanonicalName().replace( '.' , '_' );

    protected List< Class<?> >  scanControllerClass(){
        // 获得需要加载的 控制器类所在的包名
        String packageName = this.getInitParameter( CONTROLLER_PACKAGE_NAME_PARAM );
        if( packageName == null || packageName.trim().isEmpty() ) {
            throw new RuntimeException( "尚未指定控制器类所在的包，请在的初始化参数中使用" + CONTROLLER_PACKAGE_NAME_PARAM +"指定该参数" );
        }
        // 创建 ClassPathResourceLoader 对象
        ClassPathResourceLoader loader = new ClassPathResourceLoader();
        // 从指定包中加载所有类
        List< Class<?> > controllerClassList = loader.scan( packageName );
        return controllerClassList ;
    }

    /** 处理指定 类 中标注有 RequestMapping 注解的方法  */
    private void prepareControllerMethod(final String classKey , Class<?> clazz ){
        // 获得 在 clazz 对应的类中声明的所有方法
        Method[] methods = clazz.getDeclaredMethods();
        if( methods != null && methods.length > 0 ) {
            Map<String, Method> map = new HashMap<>();
            for (Method m : methods) {
                // 获得 m 对应的方法上的 RequestMapping 注解
                RequestMapping mapping = m.getDeclaredAnnotation(RequestMapping.class);
                // 如果 m 对应的方法标注了 RequestMapping 注解，则 mapping 一定不为 null
                if (mapping != null) {
                    // 获取 RequestMapping 注解中 path 属性的值
                    String methodKey = mapping.path();
                    // 将 methodKey - m 键值对 放入到 新创建的集合 methodMap 集合中
                    map.put(methodKey, m);
                }
            }
            // 将 clazz 对应的类中所有标注了 RequestMapping 注解的方法组成的 Map 集合添加到 methodMaps 集合中
            methodMaps.put(classKey, map);
        }
    }

    /**  */
    protected void prepareControllerClass(List<Class<?>> list ) {

        if( list != null && list.size() > 0  ) {
            // 迭代 list 集合
            for( Class<?> c : list ) {
                // 获取 c 对应的 类中所标注的类型为 @Controller 的注解
                Controller anno = c.getDeclaredAnnotation( Controller.class );
                // 如果从 c 对应的类上获取到 Controller 注解，则 annotation 一定不为 null
                if( anno != null ) {
                    // 获取 c 对应的 类中上  Controller 注解 的 path 属性的值
                    final String path = anno.path() ; // "/customer"
                    // 剔除 path 路径中的 / 仅保留剩余部分
                    String classKey = path.substring( 1 ) ; // "customer"
                    // 如果 在 controllerClassMap 已经包含了 指定的 classKey
                    if( controllerClassMap.containsKey( classKey ) ) {
                        Class<?> clazz = controllerClassMap.get( classKey );
                        throw new RuntimeException( "在 " + c.getCanonicalName() +" 类和 " + clazz.getCanonicalName() + " 类中使用了相同的 路径 [ path = /" + classKey + " ]" );
                    }

                    // 处理  c 对应的类中声明的所有方法
                    this.prepareControllerMethod( classKey , c );

                    // 将 classKey - Class 键值对 放入到 controllerClassMap 集合中
                    controllerClassMap.put( classKey , c );
                }
            }
        }
    }

    /**  专门用来从 uri 中获取本次所请求的资源路径  */
    protected String extractURI() {
        HttpServletRequest request = RequestHolder.getCurrentRequest() ;// 从当前线程中获取请求对象
        final String uri = request.getRequestURI() ; // 获取本次请求对应的路径(URI)

        final String path ;
        if( DELIMITER_SLASH.equals( contextPath ) ) {
            // 如果 contextPath 只有一个 / 则表示该应用是当前 Web 容器的默认应用
            // 默认情况下，在 Tomcat 中直接使用 http://localhost:8080 访问的是 webapps/ROOT
            path = uri ;
        } else {
            // 如果 contextPath 是形如 /app 形式的，则将这部分剔除掉
            path = uri.substring( contextPath.length() );
        }
        return path ;
    }

    /** 从请求路径中抽取 与 控制器类 上标注的 RequestMapping 注解的 path 值相对应的一部分 */
    protected String extractControllerPath(){
        // 调用 extractURI 方法剔除 本次请求路径中 contextPath 对应的那部分内容
        String uri = this.extractURI() ;
        // 假设 extractURI 抽取后的 uri 为  "/customer/page/regist"
        String s = uri.substring( 1 ) ;
        String classKey = s.substring( 0 , s.indexOf( "/" ) ) ;
        return classKey ;
    }

    /** 从路径中抽取与 @RequestMapping 中 path 属性对应的那部分内容  */
    protected String extractMethodPath() {
        // 调用 extractURI 方法剔除 本次请求路径中 contextPath 对应的那部分内容
        String uri = this.extractURI() ;
        // 假设 extractURI 抽取后的 uri 为  "/customer/page/regist"
        String s = uri.substring( 1 ) ; // 从第一个字符开始截取至末尾，结果为 "customer/page/regist"
        // 随后从 "customer/page/regist" 中截取 第二个 / 之后的所有内容，得到 "/page/regist"
        String path = s.substring( s.indexOf(DELIMITER_SLASH) ) ;
        return path ;
    }

    /**
     * 通过反射调用 当前对象 ( this ) 的 指定方法
     * @param m 被执行的方法对应的 Method 对象
     * @param args 被执行的方法在执行时所传入的实际参数
     * @return 返回 m 所表示的方法所返回的值
     */
    private Object invokeControllerMethod(Object controller , Method m , Object... args ) throws RuntimeException {
        try {
            return m.invoke( controller , args ) ;
        } catch (IllegalAccessException e) {
            throw new RuntimeException( "非法访问" , e );
        } catch (IllegalArgumentException e) {
            throw new RuntimeException( "参数非法" , e );
        } catch (InvocationTargetException e) {
            throw new RuntimeException( e );
        }
    }

    /** 根据被调用的控制器方法的参数类型来创建相应的对象，以便于为被调用的控制器方法传递实参 */
    private Object actualParameter( Class<?> paramType ){

        if( paramType == ParameterHolder.class ) {
            return ParameterHolder.getInstance() ;
        }

        if( paramType == RequestContext.class ) {
            return RequestContext.getInstance() ;
        }

        if( paramType == RedirectAttributes.class ){
            RedirectAttributes attributes = new RedirectAttributes(); // 创建 RedirectAttributes 实例
            HttpSession session = RequestHolder.getCurrentRequest().getSession(); // 获取当前请求关联的会话对象
            // 将 RedirectAttributes 对象添加到 会话对象 中 ( 以 this.redirectAttributesName 为属性名称 )
            session.setAttribute( this.redirectAttributesName , attributes );
            return attributes ;
        }

        if( paramType == ServletRequest.class || paramType == HttpServletRequest.class ) {
            return RequestHolder.getCurrentRequest() ;
        }

        if( paramType == ServletResponse.class || paramType == HttpServletResponse.class ) {
            return ResponseHolder.getCurrentResponse();
        }

        if( paramType == HttpSession.class ) {
            return RequestHolder.getCurrentRequest().getSession();
        }

        if( paramType == ServletContext.class ) {
            return this.application ;
        }

        if( paramType == Map.class ) {
            return new HashMap<String,Object>();
        }

        throw  new IllegalArgumentException( "当前的框架不支持在控制器方法中 " + paramType.getSimpleName() + " 类型的参数");

    }

    protected Object process(Object controller , Method method ) throws RuntimeException {
        // 获取即将执行的 method 的所有参数
        Class<?>[] paramTypes = method.getParameterTypes();

        if ( paramTypes.length == 0 ) {
            // 如果被调用的方法没有任何参数，则直接调用该方法即可
            // 在被调用的方法内部需要使用  request 和  response 时可以通过 getRequest() 和 getResponse() 来获取
            return this.invokeControllerMethod(controller, method);
        } else {
            List<Object> actualParams = new ArrayList<>(); // 创建一个集合用来缓存实参
            // 逐个处理参数
            for( Class<?> type : paramTypes ){
                try {
                    Object actual = this.actualParameter(type); // 根据参数类型准备相应的实参
                    actualParams.add( actual ); // 将实参添加到实参列表中
                } catch ( IllegalArgumentException e ) {
                    throw new RuntimeException( "方法 " + method.toString() + " 使用了 " + type.getSimpleName() + "类型的参数" , e );
                }
            }
            return this.invokeControllerMethod( controller, method , actualParams.toArray() );
        }
    }

    /** 清理会话对象中的 RedirectAttributes 对象 */
    protected final void clearRedirectAttributes(){
        HttpServletRequest request =RequestHolder.getCurrentRequest(); // 获得请求对象
        HttpSession session = request.getSession(); // 获得当前请求关联的 会话对象
        // 根据约定的 RedirectAttributes 的属性名称，尝试从 当前会话对象中获取 FlashMap
        Object o = session.getAttribute( this.redirectAttributesName );
        // 如果 会话对象中已经存在 RedirectAttributes 对象
        if( o instanceof  RedirectAttributes ) {
            RedirectAttributes attributes = (RedirectAttributes) o ;
            // 获取 RedirectAttributes 对象中所有的 Flash Attribute 的名称
            Set<String> attributeNames = attributes.getFlashAttributeNames();
            // 将 RedirectAttributes 对象中所有的 Flash Attribute 添加到当前请求对象中
            for( String name : attributeNames  ) {
                Object value = attributes.getFlashAttribute( name ) ; // 根据 名称 从 RedirectAttributes 中取出相应的值
                request.setAttribute( name , value ); // 将 Flash Attribute 添加到当前请求对象中
            }

            // 最重要的一步，根据 约定的 RedirectAttributes 的属性名称，将 RedirectAttributes 从 会话对象 中删除
            session.removeAttribute( this.redirectAttributesName );

        }
        // 如果 会话对象中 根本不存在 FlashMap 对象，则什么都不处理
    }

    private void handleString( String result  ) throws ServletException, IOException {
        if( ( result = result.trim() ).isEmpty() ) {
            throw new RuntimeException( "控制器方法不能返回空白字符串" );
        }
        HttpServletRequest request = RequestHolder.getCurrentRequest() ;
        HttpServletResponse response = ResponseHolder.getCurrentResponse();
        if( result.contains(DELIMITER_CLN) ){ // 如果控制器方法返回的结果中包含 冒号
            int index = result.indexOf( DELIMITER_CLN);
            String type = result.substring( 0 , index );
            String path = result.substring( index + 1 );
            // 如果控制器方法返回的结果以 forward 为前缀
            if( "forward".equalsIgnoreCase( type ) ) {
                request.getRequestDispatcher( path ).forward( request , response );
                return ;
            }
            if ( "redirect".equalsIgnoreCase( type ) ) {
                response.sendRedirect( path );
                return ;
            }
            throw new RuntimeException( "当前框架暂时不支持 " + type  + " 操作" );
        } else { // 如果控制器方法返回的结果中不包含 冒号
            // 默认执行 转发操作
            request.getRequestDispatcher( result ).forward( request , response );
        }
    }

    private void handleMap( Map map ) throws ServletException, IOException {
        String json = JSON.toJSONString( map );
        HttpServletResponse response = ResponseHolder.getCurrentResponse();
        response.setContentType( "text/plain;charset=UTF-8" );
        response.getWriter().println( json );
    }

    protected void handle( Method method , Object result ) throws ServletException, IOException {

        Class<?> returnType =  method.getReturnType();
        if( returnType == void.class ) {
            return ;
        }

        if( result == null ) {
            throw new RuntimeException( "方法 " + method.toString() + " 不能直接返回 null " );
        }

        if( result instanceof  String ) {
            String uri = (String) result ;
            this.handleString( uri );
            return ;
        }

        if( result instanceof  Map ) {
            Map map = (Map) result ;
            this.handleMap( map );
            return ;
        }

    }

    /** 根据指定的控制器类型创建控制器实例 */
    protected Object createControllerObject(Class<?> c ) {
        try {
            // 从 c 对应的类中获取无参构造
            Constructor<?> constructor = c.getConstructor();
            return constructor.newInstance(); // 调用 c 对应的类的无参构造创建实例
        } catch (NoSuchMethodException e) {
            throw new RuntimeException( "在 " + c.getCanonicalName()+ " 类中未找到无参构造" , e );
        } catch (IllegalAccessException e) {
            throw new RuntimeException( "非法访问" , e );
        } catch (InstantiationException e) {
            throw new RuntimeException( "实例化失败" , e );
        } catch (InvocationTargetException e) {
            throw new RuntimeException( e );
        }
    }

    /** 初始化 default servlet 的名称 */
    protected void initDefaultServletName(){
        // 常量 DEFAULT_SERVLET_NAME_PARAM 指定了在 web.xml 中通过 context-param 指定 default servlet 时的参数名称
        defaultServletName = application.getInitParameter( DEFAULT_SERVLET_NAME_PARAM  );
        if( defaultServletName == null || defaultServletName.trim().isEmpty() ) {
            defaultServletName = "default" ; // 绝大多数容器提供的 default servlet 的 servlet-name 为 default
        }
    }

    /** 调用 由 容器所提供的 default servlet  */
    protected void invokeDefaultServlet( HttpServletRequest request , HttpServletResponse response  )
            throws ServletException, IOException {
        // 如果 请求路径 不 符合指定的模式 ( PATH_PATTERN ) ，或者 当前 Servlet 无法处理指定请求，就 找到 由容器中提供的 default servlet 来处理
        RequestDispatcher dispatcher = application.getNamedDispatcher( defaultServletName );
        // 通过 default servlet 来完成 转发操作
        dispatcher.forward(request, response);
    }

}
