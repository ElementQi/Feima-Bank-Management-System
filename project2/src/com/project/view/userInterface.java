package com.project.view;

import com.project.dao.UserDao;
import com.project.model.User;
import com.project.util.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

public class userInterface extends JDialog{
    private JButton btCheckBalance;
    private JButton btAccess;
    private JButton btStore;
    private JButton btTransfer;
    private JButton btChange;
    private JButton btRuin;
    private JPanel userInterfacePanel;

    public userInterface(JFrame parent, User user) {

        super(parent);
        setTitle("飞马银行用户界面");
        setContentPane(userInterfacePanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);

        // 查询余额按钮
        btCheckBalance.addActionListener(e -> {

            // 获取数据库中当前用户信息
            User currentUser=currentUser(user);

            JOptionPane.showMessageDialog(this,
                    currentUser.getUserName()+"\n你的账户余额为："+currentUser.getBalance(),
                    "查询结果",
                    JOptionPane.INFORMATION_MESSAGE);

        });

        // 取钱
        btAccess.addActionListener(e -> {
            AccessMoney accessFrame=new AccessMoney(null, user);

        });

        // 存钱
        btStore.addActionListener(e -> {
            StoreMoney storeFrame=new StoreMoney(null, user);

        });

        // 转账
        btTransfer.addActionListener(e -> {
            TransferMoney transferFrame=new TransferMoney(null, user);

        });

        // 修改信息
        btChange.addActionListener(e -> {
            ChangeInformation changeInformationFrame=new ChangeInformation(null, user);

        });

        // 销户
        btRuin.addActionListener(e -> {
            RuinAccount ruinAccountFrame=new RuinAccount(null, user);
            if(!ruinAccountFrame.isActive()) dispose();

        });

        // 关闭按钮将会结束进程
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);

    }

    public User currentUser(User loginedUser){

        UserDao userDao=new UserDao();
        User resultUser=null;
        try {
            // 连接数据库
            Connection con=DbUtil.getCon();
            // 登录用户
            resultUser=userDao.loginUser(con, loginedUser);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return resultUser;
    }

    public static void main(String[] args) {
        userInterface myUser=new userInterface(null, new User());
    }
}
