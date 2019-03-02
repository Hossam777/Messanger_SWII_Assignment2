package MainPackage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {

    private JButton connect;
    private JPanel panel1;
    public MainForm(){
        connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //connect to server and peers
            }
        });
    }
    public static void main(String[] args){
        JFrame jFrame = new JFrame("Messanger");
        jFrame.setContentPane(new MainForm().panel1);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
    }
    @Override
    public void finalize() throws Throwable {
        //close connections
        super.finalize();
    }

}
