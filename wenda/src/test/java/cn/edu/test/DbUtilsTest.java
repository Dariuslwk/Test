package cn.edu.test;

import cn.edu.ecut.entity.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DbUtilsTest {

    private Connection conn ;

    public @Before void prepare() throws Exception {
        String driverClassName = "com.mysql.cj.jdbc.Driver" ;
        // jdbc:mysql://localhost:3306/数据库名?useSSL=false
        String jdbcURL = "jdbc:mysql://localhost:3306/wenda?useSSL=false&serverTimezone=Asia/Shanghai" ;
        String jdbcUser = "root" ;
        String jdbcPassword = "" ;
        // 1、加载并注册驱动
        Class.forName( driverClassName );
        // 2、建立数据库连接
        conn =  DriverManager.getConnection( jdbcURL , jdbcUser , jdbcPassword );
        System.out.println( "connection => " + conn );
    }

    public @Test void findUser1() throws SQLException {

        // id 是主键，它是 非空 且 惟一
        String select = "SELECT id , login_name , password , nickname , gender , register_time FROM t_users WHERE id = ? " ;

        QueryRunner runner = new QueryRunner();

        // 创建一个实现了 ResultSetHandler接口的匿名内部类的实例
        // 通过该实例的 handle 方法将 结果集中的数据 包装成 一个 User 对象
        ResultSetHandler<User> handler = new ResultSetHandler<>(){
            @Override
            public User handle( ResultSet rs ) throws SQLException {
                User user = null ; // 声明一个 User 类型的变量

                if( rs.next() ) { // 如果结果集的下一行有待处理的数据

                    user = new User(); // 创建一个 User 实例并将其赋值到 user 变量中

                    int id = rs.getInt("id");
                    user.setId(id);

                    String loginName = rs.getString("login_name");
                    user.setLoginName(loginName);

                    String password = rs.getString("password");
                    user.setPassword(password);

                    String nickname = rs.getString("nickname");
                    user.setNickname(nickname);

                    String gender = rs.getString("gender");
                    if (gender != null) {
                        char g = gender.charAt(0);
                        user.setGender(g);
                    }

                    java.sql.Date date = rs.getDate("register_time");
                    LocalDate registerTime = date.toLocalDate();
                    user.setRegisterTime(registerTime);
                }

                return user;
            }
        };

        // runner.query( connection ,  SQL , resultSetHandler , params... );
        User u = runner.query( conn , select , handler , 500 );
        System.out.println( u );
        Assert.assertNotNull( u );
        System.out.println( u.getLoginName() + " , " + u.getNickname() );
    }

    public @Test void findUser2() throws SQLException {

        // login_name 列含有 unique 约束，因此也是惟一的
        String select = "SELECT id , nickname , register_time FROM t_users WHERE login_name = ? " ;

        QueryRunner runner = new QueryRunner();

        ResultSetHandler<User> handler = new BeanHandler<>( User.class ) {
            @Override
            public User handle(ResultSet rs) throws SQLException {
                User user = null ; // 声明一个 User 类型的变量

                if( rs.next() ) { // 如果结果集的下一行有待处理的数据

                    user = new User(); // 创建一个 User 实例并将其赋值到 user 变量中

                    int id = rs.getInt("id");
                    user.setId(id);

                    String nickname = rs.getString("nickname");
                    user.setNickname(nickname);

                    java.sql.Date date = rs.getDate("register_time");
                    LocalDate registerTime = date.toLocalDate();
                    user.setRegisterTime(registerTime);
                }

                return user;
            }
        };

        User u = runner.query( conn , select , handler , "minmin" );
        System.out.println( u );

        System.out.println( u.getId() );
        System.out.println( u.getRegisterTime() );

    }

    public @After void release() {
        if( conn != null ) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
