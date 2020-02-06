package org.malajava.web.context;

import javax.servlet.http.HttpServletResponse;

public class ResponseHolder {

    /** 使用线程局部变量来保存 当前响应对象 */
    private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();

    /** 获取当前线程关联的响应对象(从线程局部变量中获取) */
    public static void setCurrentResponse(HttpServletResponse response) {
        responseHolder.set(response);
    }

    public static HttpServletResponse getCurrentResponse() {
        return responseHolder.get();
    }

    public static void removeCurrentResponse() {
        responseHolder.remove();
    }

}
