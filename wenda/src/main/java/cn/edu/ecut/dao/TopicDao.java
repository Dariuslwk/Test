package cn.edu.ecut.dao;

import cn.edu.ecut.entity.Topic;
import cn.edu.ecut.entity.User;
import cn.edu.ecut.utils.JdbcHelper;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
  CREATE TABLE t_topics (
    id  INT(8)  auto_increment ,
    title VARCHAR(100) ,
    content TEXT ,
    ask_time TIMESTAMP ,
    address VARCHAR(40) ,
    user_id INT(5) ,
    PRIMARY KEY( id ) ,
    FOREIGN KEY( user_id ) REFERENCES t_users( id )
  );
 */
public class TopicDao {

    private UserDao userDao = new UserDao(); // 应该增加 Service 层，并在 Service 层完成对 Topic 和 User 的处理

    private ResultSetHandler< List<Topic> > batchHandler = new ResultSetHandler<List<Topic>>() {
        @Override
        public List<Topic> handle(ResultSet rs) throws SQLException {
            List<Topic> list = new ArrayList<>();
            while( rs.next() ){
                Topic topic = wrap(rs);
                list.add( topic );
            }
            return list;
        }
    } ;

    private Topic wrap(ResultSet rs) throws SQLException {
        Topic topic = new Topic();

        int id = rs.getInt("id");
        topic.setId(id);

        topic.setTitle(rs.getString("title"));
        topic.setContent(rs.getString("content"));
        topic.setAddress(rs.getString("address"));
        /*【 ~ ~ ~ ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~ 】*/
        int userId = rs.getInt("user_id");
        User user = userDao.find(userId);
        topic.setUser(user);
        /*【 ~ ~ ~ ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~ 】*/
        java.sql.Timestamp time = rs.getTimestamp("ask_time");
        if (time != null) {
            java.time.LocalDateTime askTime = time.toLocalDateTime();
            topic.setAskTime(askTime);
        }
        return topic;
    }

    private ResultSetHandler<Topic> singleHandler = new ResultSetHandler<Topic>() {
        @Override
        public Topic handle(ResultSet rs) throws SQLException {
            Topic topic = null ;
            if( rs.next() ) {
                topic = wrap(rs);
            }
            return topic;
        }
    };

    public List<Topic> list(){
        String sql = "SELECT id , title , content , ask_time , address , user_id FROM t_topics ORDER BY ask_time DESC" ;
        List<Topic> list = JdbcHelper.select( sql , batchHandler ) ;
        return list ;
    }

    public Topic find( int id ) {
        String sql = "SELECT id , title , content , ask_time , address , user_id FROM t_topics WHERE id = ? " ;
        Topic topic = JdbcHelper.select( sql , singleHandler , id ) ;
        return topic ;
    }

    public boolean save( Topic topic ){

        String sql = "INSERT INTO t_topics " +
                          " ( title , content , ask_time , address , user_id ) " +
                          " VALUES ( ? , ? , ? , ? , ? )" ;

        // java.sql.Date <=====>  java.time.LocalDate
        // java.sql.Time <=====>  java.time.LocalTime
        // java.sql.Timestamp <=====>  java.time.LocalDateTime
        java.sql.Timestamp time = null ;
        java.time.LocalDateTime t = topic.getAskTime();
        if( t != null ) {
            time = java.sql.Timestamp.valueOf( t );
        }

        /*【 ~ ~ ~ ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~ 】*/
        Integer userId = null ;
        User author = topic.getUser();
        if( author != null ) {
            userId = author.getId() ;
        }

        Object[] params = { topic.getTitle() ,topic.getContent() , time , topic.getAddress() , userId };
        /*【 ~ ~ ~ ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~  ~ ~ ~ 】*/

        long id = JdbcHelper.insert( sql , params);
        return id > 0  ;
    }

}
