package MainPackage;

import MainPackage.Classes.TrackerPostIp;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class MainForm {

    private JButton connect;
    private JPanel panel1;
    private static JFrame jFrame;
    private static TrackerPostIp trackerPostIp;

    public MainForm(){
        connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //connect to server and peers
                try {
                    trackerPostIp = new TrackerPostIp();
                    InetAddress myip=java.net.InetAddress.getLocalHost();
                    trackerPostIp.postIp(myip.getHostAddress(), new TrackerPostIp.CallBack() {
                        public void completed(JSONArray jsonArray) {

                            jFrame.setVisible(false);
                        }

                        public void failed() {

                        }
                    });
                    new peersDetails(myip.getHostAddress());
                } catch (Exception e1) {
                    e1.printStackTrace();
                    //handle Socket not responding

                }
            }
        });
    }
    public static void main(String[] args) throws Exception{
        jFrame = new JFrame("Messanger");
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
