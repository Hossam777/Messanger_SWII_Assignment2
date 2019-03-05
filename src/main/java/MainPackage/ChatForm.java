package MainPackage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatForm<ovveride> {

    private JPanel jPanel;
    private JLabel chathistory;
    private JTextArea messageText;
    private JButton Sendbtn;
    private JFrame jFrame;
    private Socket socket;
    final String sourceIp;
    public ChatForm(String sourceIp,String destinationIp) throws Exception{
        jFrame = new JFrame(destinationIp);
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
        socket = new Socket(destinationIp,4777);
        this.sourceIp = sourceIp;
        Sendbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //send message
                sendMessage(messageText.getText());
            }
        });
    }
    public boolean sendMessage(String message){
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("messagefrom:" + sourceIp + ";" + message);
            dataOutputStream.flush();
            dataOutputStream.close();
            return true;
        } catch (IOException e) {
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
