package com.project.view;

import com.project.dao.UserDao;
import com.project.model.User;
import com.project.util.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;


public class RuinAccount extends JDialog {
    private JButton btNo;
    private JButton btYes;
    private JPanel ruinPanel;

    public RuinAccount(JFrame parent, User user) {

        super(parent);
        setTitle("销户");
        setContentPane(ruinPanel);
        setMinimumSize(new Dimension(300, 300));
        setModal(true);
        setLocationRelativeTo(parent);

        btYes.addActionListener(evt -> {

            // 接入数据库
            DbUtil dbUtil = new DbUtil();
            try {
                // 连接数据库
                Connection con = dbUtil.getCon();
                // 进行取钱处理，对数据库数据进行修改
                UserDao.deleteUserByPersonId(con, user.getPersonId());

                JOptionPane.showMessageDialog(this,
                        "成功销户："+user.getPersonId(),
                        "结果",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();


            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        btNo.addActionListener(evt -> {
            dispose();
        });


        setVisible(true);
    }
}
