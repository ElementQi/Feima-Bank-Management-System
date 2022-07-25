package com.project.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据库工具类
 */
public class DbUtil {
    private static final String dbUrl="jdbc:mysql://114.115.251.234:3306/db_bank";
    private static final String dbUserName="root"; // 用户名
    private static final String dbPassword="623abcDEF!!!"; // 密码
//    private String jdbcName="com.mysql.jdbc.Driver"; // 驱动名称
    private static final String jdbcName="com.mysql.cj.jdbc.Driver"; // 驱动名称

    public static Connection getCon()throws Exception{
        Class.forName(jdbcName);
        Connection con=DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        return con;
    }


    public void closeCon(Connection con)throws Exception{
        if(con!=null){
            con.close();
        }
    }

    public static void main(String[] args){
        DbUtil dbUtil=new DbUtil();
        try {
//            dbUtil.getCon();
            DbUtil.getCon();
            System.out.println("connection successful");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connection failed");
        }

    }

}
