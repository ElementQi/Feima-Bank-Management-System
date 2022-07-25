import com.project.view.LoginFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// 主函数入口，用于开启登录界面
public class Main {
    public static void main(String[] args) {

        LoginFrame myLogin=new LoginFrame(null);
        // 设置关闭按钮监听器，当关闭时，退出程序
        myLogin.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}