package test;

import cn.edu.ecut.dao.UserDao;
import cn.edu.ecut.entity.User;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

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

    public @Test void testSave(){

        User u = new User();

        u.setLoginName( "hanxiaozhao" );
        u.setNickname( "小昭" );
        u.setName( "韩小昭" );
        u.setPassword( "woshixiaozhao" );
        u.setGender( '女' );
        java.time.LocalDate birthdate = LocalDate.of( 1997 , 6 , 7 ) ;
        u.setBirthdate( birthdate );
        u.setRegisterTime( LocalDate.now() );

        boolean x = userDao.save( u );
        System.out.println( x );

    }

}
