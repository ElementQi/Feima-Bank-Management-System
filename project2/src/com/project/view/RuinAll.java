package com.project.view;

import com.project.dao.UserDao;
import com.project.util.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDate;


public class RuinAll extends JDialog {
    private JButton btNo;
    private JButton btYes;
    private JPanel ruinAllPanel;

    public RuinAll(JFrame parent) {

        super(parent);
        setTitle("批量销户");
        setContentPane(ruinAllPanel);
        setMinimumSize(new Dimension(600, 400));
        setModal(true);
        setLocationRelativeTo(parent);

        // 监听取消按钮
        btNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // 监听确定按钮
        btYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // 获取当前时间，当前时间-70年即为出生日期的阈值
                // select * from t_user where birthDate < calDate

                LocalDate thresholdDay=LocalDate.now().minusYears(70);
                try {
                    // 连接数据库
                    Connection con=DbUtil.getCon();
                    // 进行取钱处理，对数据库数据进行修改
                    int resultDeleted=UserDao.deleteUserByBirthDate(con, thresholdDay.toString());

                    if(resultDeleted==0){
                        // 销户失败
                        JOptionPane.showMessageDialog(RuinAll.this,
                                "不存在满足销户条件的用户",
                                "失败",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        // 删除成功，进行提醒
                        JOptionPane.showMessageDialog(RuinAll.this,
                                "销户数量：" + resultDeleted,
                                "成功",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        setVisible(true);

    }

    public static void main(String[] args) {
        RuinAll myFrame=new RuinAll(null);
    }
}