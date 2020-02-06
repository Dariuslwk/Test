package org.malajava.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public final class ParameterHolder {
    
    private HttpServletRequest request ;

    private ParameterHolder(  HttpServletRequest request ) {
        this.request = request ;
    }

    /** 返回一个封装了当前请求对象的 ParameterHolder 实例 */
    public static ParameterHolder getInstance(){
        return new ParameterHolder( RequestHolder.getCurrentRequest()  );
    }


    /**  获取本次请求所采用的请求方式，比如 GET 、POST 等  */
    public  String getRequestMethod() {
        return RequestHolder.getCurrentRequest().getMethod();
    }

    /**
     * 尝试从 请求对象中获取 String  类型的参数值
     * @param parameterName 请求参数的名称
     * @return 返回请求参数的 String 类型数值
     */
    public  String getString( String parameterName ) {
        HttpServletRequest request = RequestHolder.getCurrentRequest() ;
        return  request.getParameter( parameterName );
    }

    /**
     * 尝试从 请求对象中获取 指定参数名称 对应的参数值 (适用于多选按钮或多选列表)
     * @param parameterName 请求参数的名称
     * @return 以数组形式返回请求参数的值
     */
    public  String[] getValues( String parameterName ) {
        HttpServletRequest request = RequestHolder.getCurrentRequest() ;
        return  request.getParameterValues( parameterName );
    }

    /**
     * 从请求对象中获取 指定参数名称 对应的 参数值 ，并将其转换为 目标类型
     * @param parameterName 请求参数的参数名称
     * @param targetClass 参数 targetClass 表示目标类型对应的 Class 对象
     * @param <T> 类型参数 T 是目标类型的具体类型，比如 Integer 类型
     */
    private   <T> T get( String parameterName , Class<T> targetClass  ) {
        HttpServletRequest request = RequestHolder.getCurrentRequest() ;
        String value = request.getParameter( parameterName );

        if( value == null  ) {
            throw new RuntimeException( "在当前请求中未找到名称为 " + parameterName + " 的请求参数" );
        }

        // 剔除字符串中的首尾空白 后检查字符串是否为空串
        if(  ( value = value.trim() ) .isEmpty() ) {
            throw new RuntimeException( "请求参数 " + parameterName + " 的取值为空白" );
        }

        try {
            // 从 包装类 中获取 valueOf(String) 方法
            Method m = targetClass.getMethod("valueOf", String.class);
            // 调用 valueOf 方法将字符串转换为相应类型
            return (T)m.invoke( targetClass , value ) ;
        } catch ( Exception e ) {
            throw  new RuntimeException( e );
        }

    }

    /**
     * 尝试从 请求对象中获取 byte 类型的参数值
     * @param parameterName 请求参数的名称
     * @return 返回请求参数的 byte 类型数值
     */
    public  Byte getByte( String parameterName ) {
        return get( parameterName , Byte.class );
    }

    /**
     * 尝试从 请求对象中获取 short 类型的参数值
     * @param parameterName 请求参数的名称
     * @return 返回请求参数的 short 类型数值
     */
    public  Short getShort( String parameterName ) {
        return get( parameterName , Short.class );
    }

    /**
     * 尝试从 请求对象中获取 int 类型的参数值
     * @param parameterName 请求参数的名称
     * @return 返回请求参数的 int 类型数值
     */
    public  Integer getInteger( String parameterName )  {
        return get( parameterName , Integer.class );
    }

    /**
     * 尝试从 请求对象中获取 long 类型的参数值
     * @param parameterName 请求参数的名称
     * @return 返回请求参数的 long 类型数值
     */
    public  Long getLong( String parameterName )  throws RuntimeException {
        return get( parameterName , Long.class );
    }

    /**
     * 尝试从 请求对象中获取 double 类型的参数值
     * @param parameterName 请求参数的名称
     * @return 返回请求参数的 double 类型数值
     * @throws RuntimeException 将字符串转换为 double 类型失败时抛出 RuntimeException
     */
    public  Float getFloat( String parameterName )  throws RuntimeException {
        return get( parameterName , Float.class );
    }

    /**
     * 尝试从 请求对象中获取 double 类型的参数值
     * @param parameterName 请求参数的名称
     * @return 返回请求参数的 double 类型数值
     * @throws RuntimeException 将字符串转换为 double 类型失败时抛出 RuntimeException
     */
    public  Double getDouble( String parameterName )  throws RuntimeException {
        return get( parameterName , Double.class );
    }

    /**
     * 尝试从 请求对象中获取 double 类型的参数值
     * @param parameterName 请求参数的名称
     * @return 返回请求参数的 double 类型数值
     * @throws RuntimeException 将字符串转换为 double 类型失败时抛出 RuntimeException
     */
    public  Boolean getBoolean( String parameterName )  throws RuntimeException {
        // 首先以字符串形式获取 请求参数的参数值
        String value = getString( parameterName ) ;

        if( value == null  ) {
            throw new RuntimeException( "在当前请求中未找到名称为 " + parameterName + " 的请求参数" );
        } else if(  ( value = value.trim() ) .isEmpty() ) {
            throw new RuntimeException( "请求参数 " + parameterName + " 的取值为空白" );
        } else if(  "true".equalsIgnoreCase( value )  ) {
            return Boolean.TRUE ;
        } else if(  "false".equalsIgnoreCase( value )  ) {
            return Boolean.FALSE ;
        } else {
            throw new RuntimeException( value + " 不是有效的 boolean 值" );
        }
    }

    /**
     * 尝试从 请求对象中获取 char 类型的参数值
     * @param parameterName 请求参数的名称
     * @return 返回请求参数的 char 类型数值
     * @throws RuntimeException 将字符串转换为 char 类型失败时抛出 RuntimeException
     */
    public  Character getChar( String parameterName )  throws RuntimeException {
        String value = getString( parameterName );
        if( value == null  ) {
            throw new RuntimeException( "在当前请求中未找到名称为 " + parameterName + " 的请求参数" );
        } else if(  ( value = value.trim() ) .isEmpty() ) {
            throw new RuntimeException( "请求参数 " + parameterName + " 的取值为空白" );
        } if(  value.length() > 1 ) {
            throw new RuntimeException( value + " 多于一个字符，不能转换为 char 类型 或 Character 类型 "  );
        } else {
            return value.charAt( 0 ) ; // 获取 value 中那个唯一的字符直接返回
        }
    }

    /** 获取所有的 请求参数 组成的 Map 集合  */
    public  Map<String,String[]> getParameters(){
        return RequestHolder.getCurrentRequest().getParameterMap();
    }

    /** 处理文件上传操作时，从表单中获取 指定名称 ( partName ) 的 <input type="file" > 对应的 Part 对象 */
    public   Part getPart(String  partName ) {
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        String method = getRequestMethod();
        if( ! "post".equalsIgnoreCase( method ) ) {
            throw new RuntimeException( "不支持以非POST方式上传数据" );
        }
        try {
            return request.getPart(partName);
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    /** 处理文件上传操作时，从表单中获取 所有的的 <input type="file" > 对应的 Part 对象组成的 集合  */
    public   Collection<Part> getParts( ) {
        HttpServletRequest request = RequestHolder.getCurrentRequest() ;
        String method = getRequestMethod();
        if( ! "post".equalsIgnoreCase( method ) ) {
            throw new RuntimeException( "不支持以非POST方式上传数据" );
        }
        try {
            return request.getParts();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

}
