package org.malajava.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 字符编码过滤器，适用于 Web 环境
 */
@WebFilter( urlPatterns = "/*"  , dispatcherTypes = { DispatcherType.REQUEST , DispatcherType.ERROR })
public class CharacterEncodingFilter implements Filter {
	
	/** 初始化参数的名称，设置初始化参数时必须指定为 encoding  */
	public static final String INIT_PARAM_ENCODING = "encoding" ;
	/** 默认的字符编码是 UTF-8 */
	public static final String DEFAULT_CHARSET = "UTF-8" ;
	
	private String encoding ;

	/***
	 * 读取初始化参数，如果是有效编码则采用初始化参数指定的编码，如果是无效编码则采用默认编码(默认编码是UTF-8)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 从 FilterConfig 对象中获取指定名称对应的初始化参数，并赋值给 encoding 字段(属性)
		this.encoding = filterConfig.getInitParameter( INIT_PARAM_ENCODING );
		// 检查 encoding 是否为 null 或 空串，如果是就选择使用 默认编码 ( UTF-8 )
		this.encoding = this.encoding == null || this.encoding.trim().isEmpty() ? DEFAULT_CHARSET : this.encoding;
		// 剔除 encoding 对应的字符串的 首、尾 空白
		this.encoding = this.encoding.trim();
		// 判断 指定的编码 是否是 JVM 所支持的有效字符编码
		this.encoding = Charset.isSupported( this.encoding)  ? this.encoding : DEFAULT_CHARSET ; 
	}

	/**
	 * 对请求和响应进行过滤，并设置 请求对象 ( request ) 和 响应对象 ( response ) 编码 ( 通过初始化参数配置 )
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 设置请求 和 响应 的字符编码
		request.setCharacterEncoding( encoding );
		response.setCharacterEncoding( encoding );
		// 通过 过滤器链 向后传递 请求 和 响应 对象
		chain.doFilter(request, response);
	}

	/**
	 * 本类中的 destory 不做任何操作
	 */
	@Override
	public void destroy() {
	}

}
