package MainPackage;

import MainPackage.Classes.FilesInterface;
import MainPackage.Classes.TrackerPostIp;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class BroadCast {
    private JPanel jPanel;
    private JLabel chathistory;
    private JTextArea messageText;
    private JButton Sendbtn;
    private JFrame jFrame;
    int counter = 0;
    private ArrayList<Socket> sockets;
    final String sourceIp;
    ArrayList<DataOutputStream> dataOutputStreams;
    private FilesInterface filesInterface;

    public BroadCast(String sourceIp, ArrayList<DataOutputStream> dataOutputStreams) throws Exception{
        jFrame = new JFrame("BroadCast");
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
        this.sourceIp = sourceIp;
        this.dataOutputStreams = dataOutputStreams;
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
        filesInterface = new FilesInterface("BroadCast");
    }
    public boolean sendMessage(String message) throws Throwable {
            for (DataOutputStream d : dataOutputStreams) {
                try {
                d.writeUTF("messagefrom:" + sourceIp + ";" + message);
                d.flush();
                //dataOutputStream.close();
                counter++;
                if(counter == 5){
                    counter = 0;
                    filesInterface.Write(sourceIp, message);
                }
            } catch (IOException e) {
                }
                return false;
            }
        return false;
    }
    public void reciveMessage(String ip , String message) throws IOException {
        chathistory.setText(chathistory.getText() + " \n " + message);
        filesInterface.Write(ip,message);
    }
    @Override
    public void finalize() throws Throwable {
        for (Socket socket : sockets) {
            socket.close();
        }
        super.finalize();
    }
}
