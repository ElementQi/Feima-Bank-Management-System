package com.project.view;

import com.project.dao.UserDao;
import com.project.model.User;
import com.project.util.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.regex.*;

import static com.project.dao.UserDao.registerUser;


public class RegisterFrame extends JDialog{
    private JPanel registerPanel;
    private JTextField tfUserName;
    private JButton btnRegister;
    private JButton btnCancel;
    private JTextField tfPersonId;
    private JTextField tfPhoneNum;
    private JRadioButton rbtnMale;
    private JRadioButton rbtnFemale;
    private JPasswordField pfPassword;
    private JTextField tfBirthDate;
    private JPasswordField pfConfirmPassword;

    public RegisterFrame(JFrame parent) {

        super(parent);
        setTitle("飞马银行注册界面");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);

        // 监听取消按钮
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // 监听注册按钮
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 警告信息列表
                String[] warnMessage=new String[7];

                String userName=tfUserName.getText();
                // 仅匹配中文汉字，至少1次，至多10次
                if(!Pattern.matches("[\\u4E00-\\u9FA5]{1,10}",userName)){
                    warnMessage[0]="用户姓名不符合标准";
                };

                // 密码至少为4位
                String password=String.valueOf(pfPassword.getPassword());
                if(!Pattern.matches(".{4,20}",password)){
                    warnMessage[1]="密码不符合标准";
                };

                // 确认密码判定
                String confirmPassword=String.valueOf(pfConfirmPassword.getPassword());
                if(!confirmPassword.equals(password)){
                    warnMessage[2]="两次密码不一样";
                }

                // 个人身份ID判定
                String personId=tfPersonId.getText();
                if(!Pattern.matches("\\d{12}",personId)){
                    warnMessage[3]="用户个人身份ID不符合标准";
                };

                // 用中国大陆的电话号码标准
                String phoneNum=tfPhoneNum.getText();
                if(!Pattern.matches("\\d{11}",phoneNum)){
                    warnMessage[4]="电话号码不符合标准";
                };

                String sex="M";
                // 如果没选性别，那么性别为空
                if(!rbtnMale.isSelected() && !rbtnFemale.isSelected()){
                    warnMessage[5]="未选择性别";
                }
                else sex=rbtnMale.isSelected()?"M":"F";

                // 生日判定
                String birthDate=tfBirthDate.getText();
                if(!Pattern.matches("((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])",birthDate)){
                    warnMessage[6]="生日不符合标准";
                };

                boolean judgeWarn=false;
                for(int i=0; i<warnMessage.length; i++){
                    if(warnMessage[i]!=null){
                        judgeWarn=true;
                        break;
                    }
                }

                if(!judgeWarn){
                    // 创建用户对象
                    User registerTheUser=new User(userName,password,personId,phoneNum,sex,birthDate);

                    // 申请接入数据库
                    // 检查数据库中是否已经存在该用户的信息
                    // 判断标准：用户身份ID 和 电话号码 是否被注册 （或逻辑）

                    if(whetherValid(registerTheUser)){
                        // 如果满足，那么注册
                        registerDatabase(registerTheUser);

                        // 注册成功弹窗
                        JOptionPane.showMessageDialog(RegisterFrame.this,
                                "恭喜你"+userName+"\n你的银行账户号为："+getAccountId(registerTheUser),
                                "恭喜！",
                                JOptionPane.INFORMATION_MESSAGE);

                        // 关闭该页面
                        dispose();

                    }
                    else {
                        // 如果数据库中已经存在该用户的信息
                        // 弹出警告页面
                        JOptionPane.showMessageDialog(RegisterFrame.this,
                                "你的个人ID或者电话号码已经被注册",
                                "再试一次",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }
                else {
                    // 将数组转化为字符串
                    StringBuffer sb=new StringBuffer();
                    for(int i=0; i<warnMessage.length; i++) {
                        if(warnMessage[i]!=null) {
                            sb.append(warnMessage[i]);
                            sb.append("\n");
                        }
                    }
                    String strWarn=sb.toString();

                    // 弹出警告页面
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            strWarn,
                            "再试一次",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        setVisible(true);
    }


    public static void registerDatabase(User user){
        try {
            Connection con=DbUtil.getCon();
            // 调用UserDao中的注册用户方法
            registerUser(con, user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String getAccountId(User user){

        try {
            Connection con=DbUtil.getCon();
            // 调用UserDao中的注册用户方法
            UserDao userDao=new UserDao();
            User soundUser=userDao.loginUser(con, user);
            // 获取用户银行卡id的数字
            int userId=soundUser.getUserId();
            // 将数字前面补0，一共10位
            return String.format("%10s", Integer.toString(userId)).replace(" ", "0");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean whetherValid(User user){
        // 实例化工具类，用于连接数据库
        try {
            Connection con=DbUtil.getCon();
            // 调用UserDao中的注册用户方法
            return UserDao.registerValid(con, user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public static void main(String[] args) {
        RegisterFrame myFrame=new RegisterFrame(null);
    }
}
