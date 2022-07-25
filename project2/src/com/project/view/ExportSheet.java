package com.project.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.project.model.User;
import com.project.util.DbUtil;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class ExportSheet extends JDialog{

    private DatePicker startDatePicker;
    private DatePicker stopDatePicker;

    private JTextField tfMinBalance;
    private JTextField tfMaxBalance;
    private JButton btConfirm;
    private JRadioButton rbtnM;
    private JRadioButton rbtnF;
    private JTextField tfFilename;
    private JTextField tfLocation;
    private JPanel exportPanel;

    public ExportSheet(JFrame parent){

        super(parent);

        // 设置时间区域
        DatePickerSettings d_settings = startDatePicker.getSettings();
        DatePickerSettings sd_settings = stopDatePicker.getSettings();
        d_settings.setLocale(Locale.ENGLISH);
        sd_settings.setLocale(Locale.ENGLISH);

        setTitle("导出数据");
        setContentPane(exportPanel);
        setMinimumSize(new Dimension(600, 400));
        setModal(true);
        setLocationRelativeTo(parent);


        // 监听确定按钮
        btConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                // 首先检测输入的文件夹路径是否正确
                String location = tfLocation.getText();
                File fileFolder = new File(location);
                boolean checkFolder = fileFolder.isDirectory();


                // 首先做对于输入信息的判断
                boolean checkMinBalance, checkMaxBalance, checkSex, checkStartDate, checkStopDate;
                boolean errorTyping = false;
                boolean errorDate = false;
                double minBalance = 0, maxBalance = 0;
                String sex = null;

                // 余额
                // 如果两个余额都是空，那么不对余额进行查找
                checkMinBalance = !(tfMinBalance.getText().equals(""));
                checkMaxBalance = !(tfMaxBalance.getText().equals(""));

                //检查余额合法性
                try {
                    if (!checkMinBalance && checkMaxBalance) {
                        maxBalance = Double.parseDouble(tfMaxBalance.getText());
                    } else if (!checkMaxBalance && checkMinBalance) {
                        minBalance = Double.parseDouble(tfMinBalance.getText());
                        maxBalance = Double.POSITIVE_INFINITY;
                    } else if (checkMinBalance) {
                        minBalance = Double.parseDouble(tfMinBalance.getText());
                        maxBalance = Double.parseDouble(tfMaxBalance.getText());
                        if (minBalance > maxBalance) {
                            errorTyping = true;
                        }
                    } else {
                        maxBalance = Double.MAX_VALUE;
                    }
                } catch (Exception err) {
                    // 检测到非法输入
                    errorTyping = true;
                }

                checkSex = !rbtnM.isSelected() && !rbtnF.isSelected();
                checkStartDate = !(startDatePicker.getText().equals(""));
                checkStopDate = !(stopDatePicker.getText().equals(""));

                // 设置性别
                if (!checkSex) {sex = rbtnM.isSelected() ? "M" : "F";}

                // 设置时间
                String startDate = startDatePicker.toString();
                String stopDate = stopDatePicker.toString();

                if(checkStartDate&&checkStopDate&&compareTime(startDate, stopDate, "yyyy-mm-dd")==1) errorDate=true;

                if(errorTyping){
                    JOptionPane.showMessageDialog(ExportSheet.this,
                            "金额区间错误！",
                            "失败",
                            JOptionPane.WARNING_MESSAGE);
                }
                else if(errorDate){
                    JOptionPane.showMessageDialog(ExportSheet.this,
                            "日期区间错误！",
                            "失败",
                            JOptionPane.WARNING_MESSAGE);
                }
                // 日期、金额输入正确
                else {
                    if (checkFolder) {
                        String filename = tfFilename.getText();
                        // 文件名如果为空则报错
                        if ("".equals(filename)) {
                            JOptionPane.showMessageDialog(ExportSheet.this,
                                    "请输入文件名！",
                                    "失败",
                                    JOptionPane.WARNING_MESSAGE);
                        } else {

                            // 接入数据库
                            // 创建成员为User的list数组
                            ArrayList<User> list = new ArrayList<>();
                            try {
                                User resultUser = null;
                                // 连接到数据库
                                Connection con = DbUtil.getCon();

                                // 查找对应数据项
                                String sql = "SELECT * " +
                                        "FROM t_user WHERE true" +
                                        (checkSex ? "" : String.format(" and sex='%s'", sex)) +
                                        (errorTyping ? "" : String.format(" and balance >= %f and balance <= %f", minBalance, maxBalance)) +
                                        (!checkStartDate ? "" : String.format(" and birthDate >= '%s'", startDate))
                                        + (!checkStopDate ? "" : String.format(" and birthDate <= '%s'", stopDate));

                                PreparedStatement pstmt = con.prepareStatement(sql);
                                ResultSet rs = pstmt.executeQuery();


                                // 将数据库返回的数据封装进User实例中并添加进list中
                                while (rs.next()) {
                                    resultUser=new User();
                                    resultUser.setUserId(rs.getInt("userId"));
                                    resultUser.setUserName(rs.getString("userName"));
                                    resultUser.setPassword(rs.getString("password"));
                                    resultUser.setPersonId(rs.getString("personId"));
                                    resultUser.setPhoneNum(rs.getString("phoneNum"));
                                    resultUser.setSex(rs.getString("sex"));
                                    resultUser.setBirthDate(rs.getString("birthDate"));
                                    resultUser.setBalance(rs.getDouble("balance"));

                                    list.add(resultUser);
                                }

                                try {

                                    // 文件名正确输入了
                                    String realLocation = location + "\\" + filename + ".xls";
                                    // 创建一个可写入的文件
                                    WritableWorkbook book = null;

                                    // 创建一个Excel文件对象
                                    book = Workbook.createWorkbook(new File(realLocation));
                                    // 创建Excel第一个选项卡对象
                                    WritableSheet sheet = book.createSheet("第一页", 0);

                                    // 设置表头，第一行内容
                                    // Label参数说明：第一个是列，第二个是行，第三个是要写入的数据值，索引值都是从0开始

                                    Label label1 = new Label(0, 0, "银行内用户Id");// 对应为第1列第1行的数据
                                    Label label2 = new Label(1, 0, "用户名");// 对应为第2列第1行的数据
                                    Label label3 = new Label(2, 0, "密码");// 对应第3列第1行的数据
                                    Label label4 = new Label(3, 0, "身份ID");// 对应为第4列第1行的数据
                                    Label label5 = new Label(4, 0, "手机号");// 对应为第5列第1行的数据
                                    Label label6 = new Label(5, 0, "性别");// 对应为第6列第1行的数据
                                    Label label7 = new Label(6, 0, "出生日期");// 对应为第7列第1行的数据
                                    Label label8 = new Label(7, 0, "账户余额");// 对应为第8列第1行的数据
                                    // 添加单元格到选项卡中
                                    sheet.addCell(label1);
                                    sheet.addCell(label2);
                                    sheet.addCell(label3);
                                    sheet.addCell(label4);
                                    sheet.addCell(label5);
                                    sheet.addCell(label6);
                                    sheet.addCell(label7);
                                    sheet.addCell(label8);

                                    // 遍历集合并添加数据到行，每行对应一个对象
                                    for (int i = 0; i < list.size(); i++) {
                                        User customer = list.get(i);
                                        // 表头占据第一行，所以下面行数是索引值+1
                                        // 跟上面添加表头一样添加单元格数据，这里为了方便直接使用链式编程
                                        sheet.addCell(new Label(0, i + 1, Integer.toString(customer.getUserId())));
                                        sheet.addCell(new Label(1, i + 1, customer.getUserName()));
                                        sheet.addCell(new Label(2, i + 1, customer.getPassword()));
                                        sheet.addCell(new Label(3, i + 1, customer.getPersonId()));
                                        sheet.addCell(new Label(4, i + 1, customer.getPhoneNum()));
                                        sheet.addCell(new Label(5, i + 1, customer.getSex()));
                                        sheet.addCell(new Label(6, i + 1, customer.getBirthDate()));
                                        sheet.addCell(new Label(7, i + 1, Double.toString(customer.getBalance())));
                                    }
                                    // 写入数据到目标文件
                                    book.write();

                                    try {
                                        // 关闭
                                        book.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    // 程序执行结束
                                    JOptionPane.showMessageDialog(ExportSheet.this,
                                            "成功导出文件！\n"+"共有"+list.size()+"个数据",
                                            "成功",
                                            JOptionPane.INFORMATION_MESSAGE);

                                    dispose();

                                } catch (Exception err) {throw new RuntimeException(err);}


                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }

                    // 如果文件夹路径不存在
                    else {
                        JOptionPane.showMessageDialog(ExportSheet.this,
                                "该文件夹不存在！",
                                "失败",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        setVisible(true);

    }


    public static int compareTime(String dateOne, String dateTwo , String dateFormatType){
        DateFormat df =new SimpleDateFormat(dateFormatType);
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        try{
            try {
                calendarStart.setTime(df.parse(dateOne));
                calendarEnd.setTime(df.parse(dateTwo));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }catch(RuntimeException e){
            throw new RuntimeException();
        }
        int result = calendarStart.compareTo(calendarEnd);
        result = Integer.compare(result, 0);
        return result ;
    }



    public static void main(String[] args) {
        ExportSheet myFrame=new ExportSheet(null);
    }

}