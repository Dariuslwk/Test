package cn.edu.test;

import cn.edu.ecut.dao.UserDao;
import cn.edu.ecut.entity.User;
import org.junit.Assert;
import org.junit.Test;

public class UserDaoTest {

    private UserDao userDao = new UserDao() ;

    public @Test void testFind1(){

        User u = userDao.find( 5 );

        Assert.assertNotNull( u );

        System.out.println( u.getLoginName() );
        System.out.println( u.getRegisterTime() );

    }

    public @Test void testFind2(){

        User u = userDao.find( "zhouzhiruo" );

        Assert.assertNotNull( u );

        System.out.println( u.getLoginName() );
        System.out.println( u.getRegisterTime() );

    }

}
