package com.project.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class adminInterface extends JDialog{
    private JButton btImport;
    private JButton btExport;
    private JButton btReport;
    private JButton btRuinAll;
    private JPanel adminInterfacePanel;

    public adminInterface(JFrame parent) {

        super(parent);
        setTitle("飞马银行管理员界面");
        setContentPane(adminInterfacePanel);
        setMinimumSize(new Dimension(500, 500));
        setModal(true);
        setLocationRelativeTo(parent);

        btImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImportSheet importSheetFrame=new ImportSheet(null);

            }
        });

        btExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExportSheet exportSheetFrame=new ExportSheet(null);

            }
        });

        btReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportFrame reportFrame=new ReportFrame(null);

            }
        });

        btRuinAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RuinAll ruinAllFrame=new RuinAll(null);

            }
        });

        // 监听关闭按钮
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        adminInterface myAdmin=new adminInterface(null);
    }
}
