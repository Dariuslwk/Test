package cn.edu.ecut.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Topic implements Serializable {

    private int id ;
    private String title ;
    private String content ;
    private LocalDateTime askTime ; // 提问时间
    private String address ; // IP地址

    // private int userId ; // 对于发起提问的用户不应该只记录用户的编号
    private User user ; // 哪个用户提问的

    private List<Solution> solutionList ; // 当前话题下的所有解答 ( 按照解答时间 降序 排列 )

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getAskTime() {
        return askTime;
    }

    public void setAskTime(LocalDateTime askTime) {
        this.askTime = askTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Solution> getSolutionList() {
        return solutionList;
    }

    public void setSolutionList(List<Solution> solutionList) {
        this.solutionList = solutionList;
    }
}
