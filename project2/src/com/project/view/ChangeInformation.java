package com.project.view;

import com.project.dao.UserDao;
import com.project.model.User;
import com.project.util.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.regex.Pattern;

import static com.project.dao.UserDao.*;

public class ChangeInformation extends JDialog{
    private JTextField tfUserName;
    private JTextField tfPhoneNum;
    private JTextField tfBirthDate;
    private JRadioButton rbtnMale;
    private JRadioButton rbtnFemale;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btConfirm;
    private JPanel changeInfoPanel;

    public ChangeInformation(JFrame parent, User ouser) {

        super(parent);
        setTitle("更改个人信息");
        setContentPane(changeInfoPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);

        // 监听确定按钮
        btConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                DbUtil dbUtilOrigin=new DbUtil();
                try {
                    // 连接数据库
                    Connection conn=dbUtilOrigin.getCon();
                    UserDao userDao=new UserDao();
                    User user=userDao.UserFromPersonId(conn, ouser.getPersonId());

                    // 警告信息列表
                    String[] warnMessage=new String[4];

                    String userName=tfUserName.getText();
                    // 仅匹配中文汉字，至少1次，至多10次
                    if(userName.equals("")) userName=user.getUserName();
                    else if(!Pattern.matches("[\\u4E00-\\u9FA5]{1,10}",userName)){
                        warnMessage[0]="用户姓名不符合标准";
                    }

                    // 密码至少为4位
                    String password=String.valueOf(pfPassword.getPassword());
                    if(password.equals("")) password=user.getPassword();
                    else if(!Pattern.matches(".{4,}",password)){
                        warnMessage[1]="密码不符合标准";
                    }

                    // 用中国大陆的电话号码标准
                    String phoneNum=tfPhoneNum.getText();
                    if(phoneNum.equals("")) phoneNum=user.getPhoneNum();
                    else if(!Pattern.matches("\\d{11}",phoneNum)){
                        warnMessage[2]="电话号码不符合标准";
                    }

                    String sex=user.getSex();
                    // 如果没选性别，那么性别为空
                    if(sex.equals("")) sex=user.getSex();
                    else if(!rbtnMale.isSelected() && !rbtnFemale.isSelected()){
                        sex=user.getSex();
                    }
                    else sex=rbtnMale.isSelected()?"M":"F";

                    // 生日判定
                    String birthDate=tfBirthDate.getText();
                    if(birthDate.equals("")) birthDate=user.getBirthDate();
                    else if(!Pattern.matches("((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])",birthDate)){
                        warnMessage[3]="生日不符合标准";
                    }

                    boolean judgeWarn=false;
                    for(int i=0; i<warnMessage.length; i++){
                        if(warnMessage[i]!=null){
                            judgeWarn=true;
                            break;
                        }
                    }

                    if(!judgeWarn){

                        // 接入数据库进行金额相减操作
                        DbUtil dbUtil=new DbUtil();
                        try {
                            // 连接数据库
                            Connection con=dbUtil.getCon();

                            boolean check=true;
                            // 如果电话是自己的电话，无所谓
                            if (phoneNum.equals(user.getPhoneNum()));
                                // 如果除了自己的电话，仍存在
                            else if(phoneExist(con, phoneNum)){
                                // 弹出警告页面
                                JOptionPane.showMessageDialog(ChangeInformation.this,
                                        "电话号码已经被注册",
                                        "再试一次",
                                        JOptionPane.ERROR_MESSAGE);
                                check=false;
                            }

                            if(check) {
                                String originPersonId=user.getPersonId();
                                User changedUser = new User(userName, password, originPersonId, phoneNum, sex, birthDate);
                                // 是自己的电话或者不存在的电话，那么执行更改操作
                                changeInfo(con, changedUser);

                                // 弹出成功界面
                                JOptionPane.showMessageDialog(ChangeInformation.this,
                                        "成功更改信息",
                                        "成功",
                                        JOptionPane.INFORMATION_MESSAGE);

                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }}
                    // 进行警告
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
                        JOptionPane.showMessageDialog(ChangeInformation.this,
                                strWarn,
                                "再试一次",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }catch (Exception e) {
                        throw new RuntimeException(e);
                    }}});

        setVisible(true);
    }
}
