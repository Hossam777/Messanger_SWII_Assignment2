package MainPackage;

import MainPackage.Classes.TrackerPostIp;
import MainPackage.Classes.FilesInterface;
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
    final String sourceIp;
    private DataOutputStream dataOutputStream;
    final String destinationIp;
    private FilesInterface filesInterface;
    int counter ;

    public ChatForm(String sourceIp, String destinationIp,DataOutputStream dataOutputStream) throws Exception{
        jFrame = new JFrame(destinationIp);
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
        counter = 0;
        this.dataOutputStream = dataOutputStream;
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
        filesInterface = new FilesInterface(destinationIp);
    }
    public boolean sendMessage(String message) throws Throwable {
        try {

            dataOutputStream.writeUTF("messagefrom:" + sourceIp + ";" + message);
            dataOutputStream.flush();
            //dataOutputStream.close();
            counter++;
            if(counter == 5){
                counter = 0;
                filesInterface.Write(sourceIp, message);
            }
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
    public void reciveMessage(String message) throws IOException {
        chathistory.setText(chathistory.getText() + " \n " + message);
        counter++;
        if(counter == 5){
            counter = 0;
            filesInterface.Write(destinationIp,message);
        }
    }
    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }
}
