package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.malajava.foundation.EncryptHelper;
import org.malajava.foundation.EncryptType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class JdbcTest2 {

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

    public @Test void findUser() throws Exception {
        String SQL = "SELECT * FROM t_users WHERE login_name = ? " ;
        // 3、创建用于 "执行" SQL 语句的 Statement 对象
        PreparedStatement ps = conn.prepareStatement( SQL );
        System.out.println( "statement => " + ps );

        // 因为 SQL 语句是含有占位符的，因此在执行SQL前需要先设置 占位符的值
        ps.setString( 1 , "yinsusu" );
        // 4、"执行" SQL
        ResultSet rs =  ps.executeQuery(); // 用于执行 查询语句
        System.out.println( "result set => " + rs );

        // 5、处理结果集
        while( rs.next() ){ // 判断 结果集中是否还有 下一行数据需要处理，如果是就返回 true ，同时导致光标移动到下一行
            int id = rs.getInt( "id" );
            String loginName = rs.getString( "login_name" );
            String password = rs.getString( "password" );

            char g = '\u0000' ;
            String gender = rs.getString( "gender" );
            if( gender != null ) {
                g = gender.charAt( 0 ); // 从字符串中获取第一个字符
            }

            java.sql.Date date = rs.getDate( "register_time" );
            LocalDate registerTime = date.toLocalDate(); // Java 8 为 java.sql.Date 增加了 toLocalDate 方法

            System.out.println( id + " , " + loginName + " , " + password + " , " + g + " , " + registerTime );
        }

        // 6、释放资源
        rs.close();
        ps.close();
    }

    public @Test void saveUser() throws Exception {

        String SQL = "INSERT INTO t_users ( login_name , password , nickname , gender , register_time ) VALUES ( ? , ? , ? , ? , ? )" ;
        // 3、创建用于 "执行" SQL 语句的 Statement 对象
        PreparedStatement ps = conn.prepareStatement( SQL );

        // 设置 "参数占位符" 的值
        ps.setString( 1 , "zhouzhiruo" );

        String password = "hello" ; // 原始密码
        String encryptedPassword = EncryptHelper.encrypt( password , EncryptType.SHA1 );
        ps.setString( 2 , encryptedPassword );

        ps.setString( 3 , "芷若妹妹");

        ps.setString( 4 , "女" );

        LocalDate current = LocalDate.now();
        java.sql.Date date = java.sql.Date.valueOf( current ) ; // Java 8 为 java.sql.Date 类提供了一个 valueOf 方法
        ps.setDate( 5 , date );

        // 执行 DML 语句并返回受该 SQL 影响的记录数目
        int count = ps.executeUpdate(); // 执行 DML 语句 ( DML : insert 、update 、delete )
        System.out.println( "count => " + count );

        ps.close();
    }

    public @After void release() throws Exception {
        if( conn != null ) {
            conn.close();
        }
    }

}
