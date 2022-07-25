package com.project.model;

public class User {
    private int userId;
    private String userName;
    private String password;
    private String personId;
    private String phoneNum;
    private String sex;
    private String birthDate;
    private double balance;

    public User(){};
    // 构造方法
    public User(String userName, String password, String personId, String phoneNum, String sex, String birthDate){
        this.userName=userName;
        this.password=password;
        this.personId=personId;
        this.phoneNum=phoneNum;
        this.sex=sex;
        this.birthDate=birthDate;
    };


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

//银行内用户Id：10位数字组成
//用户名：填姓名（10个汉字长度）
//密码：不少于4位
//身份ID：填学号（12位数字）
//手机号：正常手机号
//性别：用一位字符（M:男，F:女）
//出生日期：YYYY-MM-DD
//账户余额：双精度范围即可