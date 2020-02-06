package cn.edu.ecut.entity;

import java.io.Serializable;
import java.time.LocalDate; // JDK 1.8 新增的类型

public class User implements Serializable {

    private int id ; // identifier 标识(zhi)符
    private String loginName ; // 登录名
    private String password ; // 密码
    private String nickname ; // 昵称
    private String name ; // 姓名
    private char gender ; // 性别
    private LocalDate birthdate ; // 出生时间
    private LocalDate registerTime ; // 注册时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public LocalDate getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDate registerTime) {
        this.registerTime = registerTime;
    }
}
