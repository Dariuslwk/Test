package org.malajava.web.context;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class RequestContext {

    private HttpServletRequest request ;

    private RequestContext( HttpServletRequest request ){
        this.request = request ;
    }

    /** 返回一个封装了当前请求对象的 RequestContext 实例 */
    public static RequestContext getInstance(){
        return new RequestContext( RequestHolder.getCurrentRequest()  );
    }

    /** 获取当前请求 */
    public  HttpServletRequest getRequest(){
        return this.request ;
    }

    /** 获取当前响应 */
    public   HttpServletResponse getResponse(){
        return ResponseHolder.getCurrentResponse();
    }

    /** 获取当前会话对象 */
    public   HttpSession getSession(){
        return request .getSession() ;
    }

    /** 获取 ServletContext 对象 */
    public   ServletContext  getApplication(){
        return request .getServletContext();
    }

    /** 获取访问者的 IP 地址 */
    public   String getAddress(){
        return request .getRemoteAddr() ;
    }

    /** 获取当前 Web 应用中某个路径 在 本地操作系统中的绝对路径  */
    public   Path getRealPath(String path ) {
        ServletContext application = request.getServletContext();
        String realPath = application.getRealPath( path );
        return Paths.get( realPath );
    }

    /**  获取本次请求所采用的协议，比如 HTTP 1.1  */
    public   String getProtocol() {
        return request .getProtocol();
    }

    /** 从当前请求对象中获取指定名称的请求报头(单个值) */
    public  String  getHeader( String headerName ) {
        return request.getHeader( headerName );
    }

    /** 从请求对象中获取指定名称的请求报头 (返回多个值 )*/
    private List<String> getHeaderValues(String headerName ) {
        List<String> headerValueList = new ArrayList<>() ;
        Enumeration<String>  headerValues = request.getHeaders( headerName );
        while( headerValues.hasMoreElements() ){
            String value = headerValues.nextElement() ;
            headerValueList.add( value );
        }
        return headerValueList;
    }

    /** 从当前请求对象中获取指定名称的请求报头(多个值) */
    public  List<String> getHeaders(String headerName ) {
        return getHeaderValues( headerName );
    }

    /** 从当前请求对象中获取所有的请求报头 */
    public  Map< String , List<String> > getHeaderValues(){
        Map< String , List<String> > headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while( headerNames.hasMoreElements() ){
            String headerName = headerNames.nextElement();
            List<String> headerValueList = getHeaderValues( headerName );
            headers.put( headerName , headerValueList );
        }
        return headers ;
    }

}
