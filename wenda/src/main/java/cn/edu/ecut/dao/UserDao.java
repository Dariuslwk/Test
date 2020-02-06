package cn.edu.ecut.dao;

import cn.edu.ecut.entity.User;
import cn.edu.ecut.utils.JdbcHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.malajava.foundation.EncryptHelper;
import org.malajava.foundation.EncryptType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDao {

    private ResultSetHandler<User> handleSingle = new ResultSetHandler<>(){
        @Override
        public User handle( ResultSet rs ) throws SQLException {
            User user = null ;
            if( rs.next() ) {
                user = new User();

                int id = rs.getInt( "id" );
                user.setId( id );

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
                if( date != null ) {
                    LocalDate registerTime = date.toLocalDate();
                    user.setRegisterTime(registerTime);
                }

            }
            return user;
        }
    } ;

    public boolean save( User user ) {

        String sql = "INSERT INTO t_users " +
                            " ( login_name , password , nickname , name , gender , birthdate , register_time) " +
                            " VALUES ( ? , ? , ? , ? , ? , ? , ? ) " ;

        String originalPassword = user.getPassword(); // 这里的原始密码是指用户在界面上输入的密码，也是没有加密的密码
        String encryptedPassword = EncryptHelper.encrypt( originalPassword , EncryptType.SHA1 ); // 加密

        String gender = null ;
        char g = user.getGender();
        if( g != '\u0000' ) {
            gender = String.valueOf( g );
        }

        java.sql.Date date = null ;
        LocalDate birthdate = user.getBirthdate();
        if( birthdate != null ) {
           date = java.sql.Date.valueOf(birthdate);
        }

        java.sql.Date rt = null ;
        LocalDate registerTime = user.getRegisterTime();
        if( registerTime != null ) {
            rt = java.sql.Date.valueOf(registerTime);
        }

        Object[] params = { user.getLoginName() ,  encryptedPassword , user.getNickname() , user.getName() , gender ,  date ,  rt };

        long id = JdbcHelper.insert( sql , params);

        return id > 0  ;
    }

    public User find( String loginName ) {
        String SQL = "SELECT * FROM t_users WHERE login_name = ? " ;
        return JdbcHelper.select( SQL , handleSingle , loginName );
    }

    public User find( int id ) {
        String SQL = "SELECT * FROM t_users WHERE id = ? " ;
        User u = JdbcHelper.select( SQL , handleSingle , id );
        return u ;
    }

}
