package cn.edu.ecut.controller;

import cn.edu.ecut.dao.SolutionDao;
import cn.edu.ecut.dao.TopicDao;
import cn.edu.ecut.entity.Solution;
import cn.edu.ecut.entity.Topic;
import cn.edu.ecut.entity.User;
import org.malajava.web.context.ParameterHolder;
import org.malajava.web.context.RequestHolder;
import org.malajava.web.support.Controller;
import org.malajava.web.support.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller( path =  "/topic" )
public class TopicController {

    private TopicDao topicDao = new TopicDao();
    private SolutionDao solutionDao = new SolutionDao();

    @RequestMapping( path = "/home/page" )
    public String home(){
        // 通过 topicDao 查询所有的 话题
        List<Topic> topics = topicDao.list();
        // 获取当前请求
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        // 将 话题列表 添加到 请求对象 中
        request.setAttribute( "topicList" , topics );
        // 将请求转发到 home.vm 页面
        return "/WEB-INF/pages/home.vm" ;
    }

    @RequestMapping( path = "/ask/page" )
    public String askPage( HttpSession session ){
        User user = (User)session.getAttribute( "USER" );
        if( user == null ) {
            return "redirect:/user/sign/in/page" ; // 如果没有登录就去往 登录页面
        } else {
            return "/WEB-INF/pages/ask.vm" ; // 只有登录用户才可以提问
        }
    }

    @RequestMapping( path = "/ask/action" )
    public String askAction( ParameterHolder holder , HttpSession session ){
        String title = holder.getString( "title" );
        String content = holder.getString( "content" );

        // 检查是否输入了 标题 和 内容

        Topic t = new Topic();
        t.setTitle( title );
        t.setContent( content );

        /*【 ~ ~ ~ ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~ 】*/
        // 从 会话对象中获取 当前登录的用户
        User user = (User)session.getAttribute( "USER" );
        t.setUser( user ) ; // 以当前用户为提问者
        /*【 ~ ~ ~ ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~ 】*/

        HttpServletRequest request =  RequestHolder.getCurrentRequest();
        String addr = request.getRemoteAddr(); // IP 地址
        t.setAddress( addr );

        LocalDateTime datetime = LocalDateTime.now();
        t.setAskTime( datetime );

        boolean x = topicDao.save( t );

        return "redirect:/topic/home/page" ;
    }

    @RequestMapping( path = "/detail" )
    public String detail( ParameterHolder holder ){
        // 获得通过 URL 传递的参数值
        Integer id = holder.getInteger( "id" );
        // 根据 id 查询指定的 Topic
        Topic t = topicDao.find( id );

        /* ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ */
        // 根据 Topic 的编号 查询 它相关所有的 Solution
        List<Solution> list = solutionDao.findByTopicId( t.getId() );
        t.setSolutionList( list );
        /* ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ */

        // 获得当前请求对象
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        // 将 查询到的 Topic 实例 添加到 请求对象的属性中
        request.setAttribute( "topic" , t );
        // 将请求转发到 detail.vm 页面
        return "/WEB-INF/pages/detail.vm" ;
    }


}
