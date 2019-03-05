package MainPackage;

import MainPackage.Classes.TrackerPostIp;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;

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
                    final InetAddress myip=java.net.InetAddress.getLocalHost();
                    trackerPostIp.postIp(myip.getHostAddress(), new TrackerPostIp.CallBack() {
                        public void completed(JSONArray jsonArray) throws Exception {
                            if(!jsonArray.getJSONObject(0).get("cod").equals("200"))
                            {
                                System.out.println(jsonArray.getJSONObject(0).get("cod"));
                                return;
                            }
                            jFrame.setVisible(false);
                            ArrayList<String> ips = new ArrayList<String>();
                            ips.add("BroadCast");
                            int i =1;
                            while(jsonArray.getJSONObject(i) != null){
                                ips.add(jsonArray.getJSONObject(i).get("ip").toString());
                            }
                            if(ips.size() <= 1){
                                new peersDetails(myip.getHostAddress(),null);
                            }else
                                new peersDetails(myip.getHostAddress(),ips);
                        }

                        public void failed() {

                        }
                    });
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
