package org.malajava.web.context;

import javax.servlet.http.HttpServletRequest;

public class RequestHolder {

    /** 使用线程局部变量来保存 当前请求对象  */
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void setCurrentRequest(HttpServletRequest request) {
        requestHolder.set(request);
    }

    /** 获取当前线程关联的请求对象(从线程局部变量中获取) */
    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    public static void removeCurrentRequest() {
        requestHolder.remove();
    }

}
