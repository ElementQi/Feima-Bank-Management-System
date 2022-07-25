package com.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.project.model.User;

/**
 * 用户Dao类
 * Data Access Object
 */

public class UserDao {

    // 传入的user是一个实例化对象
    public User loginUser(Connection con, User user) throws Exception {
        User resultUser = null;
        // 准备执行sql语句，填充sql语句
        String sql = "SELECT * FROM t_user WHERE personId=? AND password=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, user.getPersonId());
        pstmt.setString(2, user.getPassword());
        ResultSet rs = pstmt.executeQuery();

        // 查看捕获到的数据
        if (rs.next()) {
            resultUser = new User();
            resultUser.setUserId(rs.getInt("userId"));
            resultUser.setUserName(rs.getString("userName"));
            resultUser.setPassword(rs.getString("password"));
            resultUser.setPersonId(rs.getString("personId"));
            resultUser.setPhoneNum(rs.getString("phoneNum"));
            resultUser.setSex(rs.getString("sex"));
            resultUser.setBirthDate(rs.getString("birthDate"));
            resultUser.setBalance(rs.getDouble("balance"));
        }

        return resultUser;
    }

    public static void registerUser(Connection con, User user) throws Exception {
        String sql = "INSERT INTO `db_bank`.`t_user` " +
                "(`userName`, `password`, `personId`, `phoneNum`, `sex`, `birthDate`, `balance`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement pstmt = con.prepareStatement(sql);
        // 将参数设置进sql语句中
        pstmt.setString(1, user.getUserName());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getPersonId());
        pstmt.setString(4, user.getPhoneNum());
        pstmt.setString(5, user.getSex());
        pstmt.setString(6, user.getBirthDate());
        // 初始资金2000
        pstmt.setString(7, "2000");
        // 执行插入操作
        pstmt.execute();

    }

    public static boolean registerValid(Connection con, User user) throws Exception {
        String sql = "SELECT * FROM t_user WHERE personId=? OR phoneNum=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, user.getPersonId());
        pstmt.setString(2, user.getPhoneNum());
        ResultSet rs = pstmt.executeQuery();

        // 如果能查到，那么返回false
        return !rs.next();

    }

    public static void changeCash(Connection con, String personId, double value) throws Exception {
        String sql = "UPDATE t_user SET balance=? WHERE personId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, Double.toString(value));
        pstmt.setString(2, personId);

        pstmt.executeUpdate();

    }

    public static boolean personExist(Connection con, String value) throws Exception {
        String sql = "SELECT * FROM t_user WHERE personId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, value);
        ResultSet rs = pstmt.executeQuery();

        // 如果能查到，那么返回true
        return rs.next();

    }

    public static double checkBalance(Connection con, String personId) throws Exception {
        String sql = "SELECT balance FROM t_user WHERE personId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, personId);

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        // 输出查询到的余额
        return rs.getDouble("balance");

    }

    public static boolean phoneExist(Connection con, String phoneNum) throws Exception {
        String sql = "SELECT * FROM t_user WHERE phoneNum=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, phoneNum);
        ResultSet rs = pstmt.executeQuery();

        // 如果能查到，返回true
        return rs.next();

    }

    public static void changeInfo(Connection con, User user) throws Exception {
        String sql = "UPDATE t_user SET `userName`=?, `password`=?, `phoneNum`=?, `sex`=?, `birthDate`=?" +
                "WHERE `personId`=?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, user.getUserName());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getPhoneNum());
        pstmt.setString(4, user.getSex());
        pstmt.setString(5, user.getBirthDate());
        pstmt.setString(6, user.getPersonId());

        pstmt.executeUpdate();

    }

    public User UserFromPersonId(Connection con, String personId) throws Exception {
        User resultUser = null;
        // 准备执行sql语句，填充sql语句
        String sql = "SELECT * FROM t_user WHERE personId=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, personId);
        ResultSet rs = pstmt.executeQuery();

        // 查看捕获到的数据
        if (rs.next()) {
            resultUser = new User();
            resultUser.setUserId(rs.getInt("userId"));
            resultUser.setUserName(rs.getString("userName"));
            resultUser.setPassword(rs.getString("password"));
            resultUser.setPersonId(rs.getString("personId"));
            resultUser.setPhoneNum(rs.getString("phoneNum"));
            resultUser.setSex(rs.getString("sex"));
            resultUser.setBirthDate(rs.getString("birthDate"));
            resultUser.setBalance(rs.getDouble("balance"));
        }

        return resultUser;
    }

    public static void deleteUserByPersonId(Connection con, String personId) throws Exception {
        String sql = "DELETE FROM t_user WHERE personId=?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, personId);

        pstmt.executeUpdate();

    }

    public static int deleteUserByBirthDate(Connection con, String threshold) throws Exception {
        String sqlCheck = "SELECT * FROM t_user WHERE birthDate<?";

        PreparedStatement pstmtCheck = con.prepareStatement(sqlCheck);
        pstmtCheck.setString(1, threshold);
        ResultSet rs = pstmtCheck.executeQuery();

        // 设置计数器，用于查看有多少出生年份小于阈值的
        int count = 0;
        while (rs.next()) count += 1;

        // 如果满足情况，则执行删除操作
        if (count > 0) {
            String sql = "DELETE FROM t_user WHERE birthDate<?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, threshold);
            pstmt.executeUpdate();
        }

        return count;

    }

    public static int checkUserNum(Connection con) throws Exception {
        String sqlCheck = "SELECT * FROM t_user";

        PreparedStatement pstmtCheck = con.prepareStatement(sqlCheck);
        ResultSet rs = pstmtCheck.executeQuery();

        // 设置计数器，用于查看有多少用户
        int count = 0;
        while (rs.next()) count += 1;
        return count;
    }

    public static double sumBalance(Connection con) throws Exception {
        String sqlCheck = "SELECT balance FROM t_user";

        PreparedStatement pstmtCheck = con.prepareStatement(sqlCheck);
        ResultSet rs = pstmtCheck.executeQuery();

        // 设置计数器，用于统计总金额
        double count = 0;
        while (rs.next()) {
            count += rs.getDouble("balance");
        }
        // 输出总金额数目
        return count;

    }
}