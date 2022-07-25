package com.project.view;

import com.project.dao.UserDao;
import com.project.model.User;
import com.project.util.DbUtil;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;


public class LoginFrame extends JDialog {
    private JPanel loginPanel;
    private JTextField tfPersonId;
    private JPasswordField pfPassword;
    private JButton btLogin;
    private JButton btRegister;

    public LoginFrame(JFrame parent){
        super(parent);
        setTitle("飞马银行登录界面");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);

        // 定义银行管理员账户的身份ID
        String superUser="000000000000";

        // 设置事件监听器，当用户点击登录按钮会发生的事情
        btLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 检测
                String personId=tfPersonId.getText();
                String password=String.valueOf(pfPassword.getPassword());

                User tempUser=new User();
                tempUser.setPersonId(personId);
                tempUser.setPassword(password);

                User user=getAuthenticatedUser(tempUser);

                // 如果登录成功，那么将会跳入用户界面
                // 分为管理员界面和普通用户界面

                if(user!=null){
                    // 挂起登录界面
                    dispose();
                    // 如果是银行管理员
                    boolean superCheck=user.getPersonId().equals(superUser);
                    if(superCheck){
                        adminInterface adminInterfaceFrame=new adminInterface(null);

                    }
                    // 如果是普通用户
                    else{
                        userInterface userInterfaceFrame=new userInterface(null, user);
                        if(!userInterfaceFrame.isActive()) setVisible(true);

                    }

                }
                // 如果用户个人ID或者密码不匹配那么会进行警告
                else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "用户个人ID或者密码不正确",
                            "再试一次",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        // 注册按钮监听器
        btRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 调用注册界面前首先隐藏登录界面
                dispose();
                // 调用注册页面
                RegisterFrame registerFrame=new RegisterFrame(null);

                // 在注册界面消失后复现登录界面
                setVisible(true);


            }
        });

        // 设置关闭按钮监听器，当关闭时，退出程序
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });


        // 在所有监听器设置完毕后才能进行对界面的可视化，不然监听器会无效
        setVisible(true);
    }

    public static User getAuthenticatedUser(User user){
        DbUtil dbUtil=new DbUtil();
        UserDao userDao=new UserDao();
        User resultUser=null;
        try {
            Connection con=dbUtil.getCon();
            resultUser=userDao.loginUser(con, user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return resultUser;

    }

    public static void main(String[] args) {
        LoginFrame myFrame=new LoginFrame(null);
    }

}
