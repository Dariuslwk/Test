package cn.edu.ecut.controller;

import cn.edu.ecut.dao.UserDao;
import cn.edu.ecut.entity.User;
import org.malajava.foundation.EncryptHelper;
import org.malajava.foundation.EncryptType;
import org.malajava.web.context.ParameterHolder;
import org.malajava.web.support.Controller;
import org.malajava.web.support.RedirectAttributes;
import org.malajava.web.support.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller( path = "/user" )
public class UserController {

    private UserDao userDao = new UserDao();

    @RequestMapping( path = "/sign/up/page")
    public String signUpPage( HttpSession session ){
        User user = (User)session.getAttribute( "USER" );
        if( user == null ) {
            return "/WEB-INF/pages/sign-up.vm" ;
        } else {
            return "redirect:/topic/home/page" ;
        }
    }

    @RequestMapping( path = "/sign/up/action" )
    public String signUpAction( ParameterHolder holder ) {
        System.out.println( "我执行了" );

        // 1、检查 用户名、密码、确认密码、验证码 是否输入，如果没有输入则返回到 注册页面

        // 2、检查 验证码是否正确，如果不正确则返回到 注册页面

        // 3、检查 两次输入密码是否一致，如果不一致就 返回到 注册页面

        // 4、检查 登录名称 是否已经在数据库中存在，如果已经存在，则返回到 注册页面

        User user = new User();

        String loginName = holder.getString( "loginName" );
        user.setLoginName( loginName );

        String password = holder.getString( "password" );
        user.setPassword( password );

        user.setRegisterTime( LocalDate.now() );

        userDao.save( user );

        return "redirect:/user/sign/in/page" ;
    }

    @RequestMapping( path = "/sign/in/page")
    public String signInPage( HttpSession session ){
        User user = (User)session.getAttribute( "USER" );
        if( user == null ) {
            return "/WEB-INF/pages/sign-in.vm" ;
        } else {
            return "redirect:/topic/home/page" ;
        }
    }

    @RequestMapping( path = "/sign/in/action" )
    public String signInAction( ParameterHolder holder , HttpSession session , RedirectAttributes  attributes ) {
        System.out.println( "正在处理用户登录" );

        final String path = "redirect:/user/sign/in/page" ; // 确定登录失败后所去往的页面

        String loginName = holder.getString( "loginName" ); // <input type="text" name="loginName" >
        String password = holder.getString( "password" );
        String captcha = holder.getString( "captcha" );
        // 1、检查 用户名、密码、验证码 是否输入，如果没有输入则返回到 登录页面
        if( loginName == null || loginName.trim().isEmpty() ) {
            attributes.addFlashAttribute( "loginNameError" , "你丫没有输入用户名" );
            return path ;
        }

        if( password == null || password.trim().isEmpty() ) {
            attributes.addFlashAttribute( "loginName" , loginName );
            attributes.addFlashAttribute( "passwordError" , "你丫没有输入密码" );
            return path ;
        }

        if( captcha == null || captcha.trim().isEmpty() ) {
            attributes.addFlashAttribute( "loginName" , loginName );
            attributes.addFlashAttribute( "captchaError" , "你丫没有输入验证码" );
            return path ;
        }

        // 2、检查验证码是否正确
        String code = (String)session.getAttribute( "CAPTCHA" );
        if( captcha.trim().equalsIgnoreCase( code ) ) {
            // 验证码正确

            // 根据 登录名称查询数据库返回相应的 User 对象
            User user = userDao.find( loginName.trim() );
            // 3、检查用户名是否正确
            if( user == null ) {
                attributes.addFlashAttribute( "loginNameError" , "你输入的用户名在系统中不存在"  );
                return path ;
            } else {
                // 对用户在页面上输入的明文密码进行加密
                String encryptedPassword = EncryptHelper.encrypt( password.trim() , EncryptType.SHA1 );
                // 4、检查密码是否正确
                // 与 从数据库中查询到的密码进行比较
                if( encryptedPassword.equals( user.getPassword() ) ) {
                    /* ********* 准予登录 ********************************************* */
                    session.setAttribute( "USER" , user  ); // 将 用户对象 保存到 会话 中
                    return "redirect:/topic/home/page" ; // "/topic/home/page" --> forward --> "/WEB-INF/pages/home.vm"
                } else {
                    attributes.addFlashAttribute( "loginName" , loginName );
                    attributes.addFlashAttribute( "passwordError" , "你输入的密码是错误的"  );
                    return path ;
                }
            }

        } else {
            attributes.addFlashAttribute( "captchaError" , "验证码输入错误"  );
            return path ;
        }
    }

    @RequestMapping( path = "/sign/out/action" )
    public String signOutAction( HttpSession session ){
        System.out.println( "用户退出" );
        session.removeAttribute( "USER" ); // 将 用户对象 从 会话 中删除
        return "redirect:/topic/home/page" ;
    }

}
