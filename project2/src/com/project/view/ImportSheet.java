package com.project.view;

import com.project.dao.UserDao;
import com.project.util.DbUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class ImportSheet extends JDialog{
    private JTextField tfLocation;
    private JButton btConfirm;
    private JPanel importPanel;

    public ImportSheet(JFrame parent){

        super(parent);
        setTitle("导入数据");
        setContentPane(importPanel);
        setMinimumSize(new Dimension(500, 500));
        setModal(true);
        setLocationRelativeTo(parent);

        btConfirm.addActionListener(evt -> {

            // 创建Excel文件对象
            Workbook book=null;
            // 首先检查文件是否存在
            String location = tfLocation.getText();
            File file = new File(location);
            if (file.exists()) {
                    // 对象赋值
                try {
                    book = Workbook.getWorkbook(file);
                    Sheet sheet = book.getSheet(0);
                    int cols=sheet.getColumns();
                    int rows=sheet.getRows();
                    String[] contents = new String[cols];

                    // 接入数据库
                    try {
                        // 连接数据库
                        Connection con = DbUtil.getCon();
                        // 进行取钱处理，对数据库数据进行修改

                        String sql="INSERT INTO `db_bank`.`t_user` " +
                                "(`userName`, `password`, `personId`, `phoneNum`, `sex`, `birthDate`, `balance`) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?);";

                        PreparedStatement pstmt=con.prepareStatement(sql);

                        int count=0;
                        for(int i=1; i<sheet.getRows(); i++) {

                            boolean duplicated=UserDao.personExist(con, sheet.getCell(2, i).getContents());
                            if(duplicated) continue;
                            else {
                                for (int j=0; j<cols; j++){
                                    contents[j] =sheet.getCell(j, i).getContents();
                                    pstmt.setString(j+1, contents[j]);
                                }
                            }

                            try{// 每结束一次统计量+1
                                // 每一行结束执行sql语句
                                pstmt.executeUpdate();
                                count+=1;
                                }
                            // 如果插入失败则跳过
                            catch (Exception e){continue;}

                        }

                        if(count==0){
                            JOptionPane.showMessageDialog(this,
                                    "数据库中已经存在这些数据！",
                                    "失败",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(this,
                                    "执行结束！\n共有" + (count) + "条记录写入数据库",
                                    "成功",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }



                } catch (IOException | BiffException e) {
                    throw new RuntimeException(e);
                }
                } else{
                JOptionPane.showMessageDialog(this,
                        "输入的文件不存在！",
                        "再试一次",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        setVisible(true);

    }

    public static void main(String[] args) {
        ImportSheet myFrame=new ImportSheet(null);

    }


}
