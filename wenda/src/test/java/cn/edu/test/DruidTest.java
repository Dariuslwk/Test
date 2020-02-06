package cn.edu.test;

import cn.edu.ecut.entity.User;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DruidTest {

    private DataSource dataSource ;

    public @Before void before(){
        // DruidDataSource 类 实现了 javax.sql.DataSource 接口
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName( "com.mysql.cj.jdbc.Driver" );
        ds.setUrl( "jdbc:mysql://localhost:3306/wenda?useSSL=false&serverTimezone=Asia/Shanghai" );
        ds.setUsername( "root" );
        ds.setPassword( "" );
        this.dataSource = ds ;
    }

    public @Test void findUser() throws SQLException {

        QueryRunner runner = new QueryRunner( dataSource );

        String select = "SELECT id , login_name AS loginName FROM t_users WHERE id = ? " ;

        ResultSetHandler<User> handler = new BeanHandler<>( User.class );

        User u = runner.query( select, handler , 5 );

        System.out.println( u.getLoginName() );
    }

}
