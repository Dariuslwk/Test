package cn.edu.ecut.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.SQLException;

// 被 final 修饰的类没有子类
public final class JdbcHelper {

    private static DataSource dataSource ; // 类变量

    // 使用 静态代码块 完成 对 类变量 的初始化
    static {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName( "com.mysql.cj.jdbc.Driver" );
        ds.setUrl( "jdbc:mysql://localhost:3306/wenda?useSSL=false&serverTimezone=Asia/Shanghai" );
        ds.setUsername( "root" );
        ds.setPassword( "" );
        dataSource = ds ;
    }

    public static <T> T select( String sql , ResultSetHandler<T> handler , Object... params) {
        QueryRunner runner = new QueryRunner( dataSource );
        try {
            return runner.query( sql , handler , params );
        } catch (SQLException cause) {
            throw new RuntimeException( "查询失败" , cause );
        }
    }

    public static long insert( String sql , Object... params ) {
        QueryRunner runner = new QueryRunner( dataSource );
        ResultSetHandler<BigInteger> handler = new ScalarHandler<>() ;
        try {
            BigInteger n = runner.insert( sql , handler , params ) ;
            long id = n.longValue() ;
            return  id ;
        } catch (SQLException cause) {
            throw new RuntimeException( "插入失败" , cause );
        }
    }

}
