package com.project.view;

import com.project.dao.UserDao;
import com.project.model.User;
import com.project.util.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.regex.Pattern;

import static com.project.dao.UserDao.*;

public class TransferMoney extends JDialog{
    private JTextField tfTargetPersonId;
    private JTextField tfTransferValue;
    private JButton btConfirm;
    private JPanel transferPanel;

    public TransferMoney(JFrame parent, User user){

        super(parent);
        setTitle("转账");
        setContentPane(transferPanel);
        setMinimumSize(new Dimension(300, 300));
        setModal(true);
        setLocationRelativeTo(parent);

        btConfirm.addActionListener(evt -> {

            // 首先针对转账对象ID进行判别
            // 个人身份ID判定
            String targetPersonId=tfTargetPersonId.getText();
            // 转账对象格式不对
            if(!Pattern.matches("\\d{12}",targetPersonId)){
                JOptionPane.showMessageDialog(this,
                        "请输入正确格式的身份ID！",
                        "再试一次",
                        JOptionPane.ERROR_MESSAGE);
            }else if(user.getPersonId().equals(targetPersonId)){
                // 转账对象不能是自己
                JOptionPane.showMessageDialog(this,
                        "不能向自己转账！",
                        "再试一次",
                        JOptionPane.ERROR_MESSAGE);
            }
            else{
                try{
                    double value=Double.parseDouble(tfTransferValue.getText());
                    System.out.println(value);

                    // 连接数据库，重新获取用户数据
                    // 接入数据库进行金额相减操作
                    DbUtil dbUtil=new DbUtil();
                    UserDao userDao=new UserDao();
                    try {
                        // 连接数据库
                        Connection con=dbUtil.getCon();
                        // 判断是否存在目标用户身份ID
                        // 存在则执行对数据库数据更改操作
                        if(personExist(con, targetPersonId)){
                            // 进行取钱处理，对数据库数据进行修改
                            User currentUser=userDao.loginUser(con, user);
                            double balance=currentUser.getBalance();

                            // 要取出的金额已经超出余额
                            if(value>balance){
                                JOptionPane.showMessageDialog(this,
                                        "您要转账的金额已经超出您的余额",
                                        "再试一次",
                                        JOptionPane.ERROR_MESSAGE);}
                            else if (value<=0) {
                                // 检测到非负数输入
                                JOptionPane.showMessageDialog(this,
                                        "请输入非负数！",
                                        "再试一次",
                                        JOptionPane.ERROR_MESSAGE);
                            }else {
                                // 进行取钱处理，对数据库数据进行修改

                                // 对当前用户进行减金额
                                changeCash(con, currentUser.getPersonId(), balance-value);
                                // 对目标用户进行加金额
                                double targetBalance=checkBalance(con, targetPersonId);
                                changeCash(con, targetPersonId, targetBalance+value);

                                // 取钱成功，进行提醒
                                JOptionPane.showMessageDialog(this,
                                        "成功向"+targetPersonId+"转出金额"+value,
                                        "成功",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                    }} catch (Exception e) {throw new RuntimeException(e);}
                } catch (Exception err){
                    // 检测到非法输入
                    System.out.println(targetPersonId);
                    System.out.println(tfTransferValue.getText());

                    JOptionPane.showMessageDialog(this,
                            "非法的输入！",
                            "再试一次",
                            JOptionPane.ERROR_MESSAGE);
                }}
        });

        setVisible(true);

    }
}
