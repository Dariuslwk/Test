package org.malajava.web.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedirectAttributes {

    // 在 RedirectAttributes 对象内部采用 Map 集合来缓存所有的 属性
    private Map<String,Object> flashMap = new HashMap<>();

    // 添加单个 Flash Attribute
    public Object addFlashAttribute( String name , Object value ) {
        return flashMap.put( name , value );
    }

    // 根据名称获取单个 Flash Attribute
    public Object getFlashAttribute( String name ) {
        return flashMap.get( name );
    }

    // 获取所有的 Flash Attribute 的名称
    public Set<String> getFlashAttributeNames(){
        return flashMap.keySet();
    }

}
