package org.malajava.web.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( ElementType.METHOD )
public @interface RequestMapping {
	
	/** 用以指定 映射路径 ( 类似于 Servlet 中的 url-pattern )  */
	String path()  ;
	
}
