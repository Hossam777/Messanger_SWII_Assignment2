package MainPackage;

import MainPackage.Classes.TrackerPostIp;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatForm<ovveride> {

    private JPanel jPanel;
    private JLabel chathistory;
    private JTextArea messageText;
    private JButton Sendbtn;
    private JFrame jFrame;
    private Socket socket;
    final String sourceIp;
    final String destinationIp;
    public ChatForm(String sourceIp, String destinationIp) throws Exception{
        jFrame = new JFrame(destinationIp);
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
        socket = new Socket(destinationIp,4777);
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        Sendbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //send message
                try {
                    sendMessage(messageText.getText());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }
    public boolean sendMessage(String message) throws Throwable {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("messagefrom:" + sourceIp + ";" + message);
            dataOutputStream.flush();
            dataOutputStream.close();
            return true;
        } catch (IOException e) {
            MainForm.tracker.deleteIp(destinationIp, new TrackerPostIp.CallBack() {
                public void completed(JSONArray jsonArray) throws Throwable {

                }

                public void failed() {

                }
            });
            e.printStackTrace();
            return false;
        }
    }
    public void reciveMessage(String message){

    }
    @Override
    public void finalize() throws Throwable {
        socket.close();
        super.finalize();
    }
}
