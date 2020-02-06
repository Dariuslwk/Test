package cn.edu.ecut.controller;

import org.malajava.web.context.ParameterHolder;
import org.malajava.web.support.Controller;
import org.malajava.web.support.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller( path = "/counsellor" )
public class CounsellorController {

    @RequestMapping( path = "/xu/shu" )
    public String xushu(){
        System.out.println( "主公，俺是徐庶，俺不能跟您一起打天下了！" );
        return "redirect:/counsellor/zhu/ge/liang" ; // 可以重定向到本地
        // return "redirect:http://www.baidu.com" ; // 也可以重定向到远程服务器
    }

    @RequestMapping( path = "/zhu/ge/liang" )
    public String zhugeliang(){
        System.out.println( "领导，啥也别说了，干！" );
        // return "/WEB-INF/success.vm" ;
        return "forward:/WEB-INF/success.vm" ;
    }

    @RequestMapping( path = "/si/ma/yi" )
    public Map<String,Object> simayi( ParameterHolder holder ) {
        System.out.println( "我是司马懿" );

        System.out.println( holder.getString( "username" ) );
        System.out.println( holder.getString( "password" ) );


        Map<String,Object> map = new HashMap<>();

        map.put( "id" , 1 ); // map.put( key , value );
        map.put( "name" , "司马懿"  );

        return map ; // Map --> alibaba : fastjson --> JSON
    }


}
