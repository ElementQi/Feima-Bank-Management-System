package com.project.view;

import com.project.dao.UserDao;
import com.project.model.User;
import com.project.util.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

import static com.project.dao.UserDao.changeCash;

public class AccessMoney extends JDialog{
    private JTextField tfValue;
    private JButton btConfirm;
    private JPanel accessPanel;

    public AccessMoney(JFrame parent, User user){

        super(parent);
        setTitle("取钱");
        setContentPane(accessPanel);
        setMinimumSize(new Dimension(300, 300));
        setModal(true);
        setLocationRelativeTo(parent);

        btConfirm.addActionListener(evt -> {

            try{
                double value=Double.parseDouble(tfValue.getText());

                // 接入数据库，重新获取用户目前的balance
                try {
                    // 连接数据库
                    Connection conn = DbUtil.getCon();
                    // 更新用户目前的余额
                    double balance =UserDao.checkBalance(conn, user.getPersonId());

                    // 要取出的金额已经超出余额
                    if(value>balance){
                        JOptionPane.showMessageDialog(this,
                                "您要取出的金额已经超出您的余额",
                                "再试一次",
                                JOptionPane.ERROR_MESSAGE);
                    } else if (value<=0) {
                        // 检测到非负数输入
                        JOptionPane.showMessageDialog(this,
                                "请输入非负数！",
                                "再试一次",
                                JOptionPane.ERROR_MESSAGE);
                    } else{
                        // 接入数据库进行金额相减操作
                        try {
                            // 连接数据库
                            Connection con=DbUtil.getCon();
                            // 进行取钱处理，对数据库数据进行修改
                            changeCash(con, user.getPersonId(), balance-value);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        // 取钱成功，进行提醒
                        JOptionPane.showMessageDialog(this,
                                "成功取出金额："+value,
                                "成功",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                }catch (Exception e) {
                    throw new RuntimeException(e);
                }}
            catch (Exception err){
                // 检测到非法输入
                JOptionPane.showMessageDialog(this,
                        "非法的输入！",
                        "再试一次",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        setVisible(true);

    }

}
