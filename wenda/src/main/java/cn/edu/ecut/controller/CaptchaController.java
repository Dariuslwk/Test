package cn.edu.ecut.controller;

import org.malajava.foundation.GraphicHelper;
import org.malajava.web.context.ResponseHolder;
import org.malajava.web.support.Controller;
import org.malajava.web.support.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;

@Controller( path = "/captcha" )
public class CaptchaController {

    @RequestMapping( path = "/produce")
    public void produce( HttpSession session ) throws Exception {
        HttpServletResponse response = ResponseHolder.getCurrentResponse(); // 获得当前的响应对象
        // 通过 response 对象获得可以 向客户端输出数据的 字节输出流
        OutputStream output = response.getOutputStream();
        // 通过调用工具类的方法产生图片验证码并输出到客户端，同时返回图片上的字符组成的字符串
        String code = GraphicHelper.create( 4 , false , 180 , 40 , output , 50 );
        System.out.println( "code => " + code );
        // 将 验证码 字符串 保存到 会话 ( session ) 对象中
        session.setAttribute( "CAPTCHA" , code );
    }

}
