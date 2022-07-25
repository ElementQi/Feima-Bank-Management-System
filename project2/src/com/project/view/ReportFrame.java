package com.project.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.dao.UserDao;
import com.project.util.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Date;

public class ReportFrame extends JDialog{
    private JTextField tfLocation;
    private JButton btConfirm;
    private JTextField tfFilename;
    private JPanel reportPanel;


    public ReportFrame(JFrame parent){

        super(parent);
        setTitle("生成报告界面");
        setContentPane(reportPanel);
        setMinimumSize(new Dimension(500, 500));
        setModal(true);
        setLocationRelativeTo(parent);

        btConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 首先检测输入的文件夹路径是否正确
                String location = tfLocation.getText();
                File fileFolder = new File(location);
                boolean checkFolder=fileFolder.isDirectory();

                if(checkFolder){
                    String filename=tfFilename.getText();
                    // 文件名如果不存在则报错
                    if("".equals(filename)){
                        JOptionPane.showMessageDialog(ReportFrame.this,
                                "请输入文件名！",
                                "失败",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        String realLocation=location+"\\"+filename+".pdf";
                        // 连接数据库
                        try {
                            Connection con = DbUtil.getCon();
                            // 获取已注册总人数
                            int pplNum=UserDao.checkUserNum(con);
                            // 获取总金额
                            double allBalance=UserDao.sumBalance(con);

                            // 创建字体对象
                            Font titleFont=new Font();
                            titleFont.setSize(32.0F);
                            // 创建文件对象
                            Document document=new Document();
                            // 调用pdf写入器
                            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(realLocation)));
                            document.open();
                            // 文件首行
                            // 由于itext5不支持中文，所以我们用英文进行输出
                            document.add(new Paragraph("\t\tFeima Bank Report", titleFont));
                            document.add(new Paragraph("*****************************************************"));
                            // 账户数目
                            document.add(new Paragraph("Signed account numer is: "+pplNum));
                            // 总金额
                            document.add(new Paragraph("Summarized bank money in total: "+allBalance));

                            // 文件导出的时间
                            Date time=new Date();
                            Paragraph p=new Paragraph("Export time:"+time);
                            document.add(p);
                            // 写入文件结束
                            document.close();

                            // 写入结束后进行提醒
                            JOptionPane.showMessageDialog(ReportFrame.this,
                                    "成功导出报告文件！",
                                    "成功",
                                    JOptionPane.INFORMATION_MESSAGE);

                            // 关闭导出界面
                            dispose();

                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

                // 文件夹输入错误
                else {
                    JOptionPane.showMessageDialog(ReportFrame.this,
                            "该文件夹不存在！",
                            "失败",
                            JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        setVisible(true);

    }




}
