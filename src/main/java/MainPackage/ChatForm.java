package MainPackage;

import MainPackage.Classes.FilesInterface;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static MainPackage.peersDetails.DisconectPeer;

public class ChatForm<ovveride> {

    private JPanel jPanel;
    private JLabel chathistory;
    private JTextArea messageText;
    private JButton Sendbtn;
    private JButton Generator;
    private JFrame jFrame;
    final String sourceIp;
    private DataOutputStream dataOutputStream;
    final String destinationIp;
    Thread generator;
    private FilesInterface filesInterface;
    ArrayList<String> messagesText;
    ArrayList<String> messagesIps;

    public ChatForm(String sourceIp, String destinationIp,DataOutputStream dataOutputStream) throws Exception{
        jFrame = new JFrame(destinationIp);
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
        messagesText = new ArrayList<String>();
        messagesIps = new ArrayList<String>();
        chathistory.setText("<html> </html>");
        this.dataOutputStream = dataOutputStream;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        generator = new Thread(new Runnable() {
            public void run() {
                double start = 1;
                double end = 5;
                try {
                    Random random = new Random();
                    while (true){
                        String message = "";
                        for(int i = 0; i < 10 ; i++){
                            message += (char)(random.nextInt(26));
                        }
                        sendMessage(message);
                        Thread.sleep((long) (start + (random.nextDouble() * (end - start))));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
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
        Generator.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(Sendbtn.isVisible()){
                    Sendbtn.setVisible(false);
                    startGenerator();
                }else{
                    endGenerator();
                    Sendbtn.setVisible(true);
                }
            }
        });
        filesInterface = new FilesInterface(destinationIp);
    }

    private void startGenerator() {
        generator.start();
    }
    private void endGenerator() {
        generator.currentThread().interrupt();
    }

    public void sendMessage(String message) throws Throwable {
        try {
            chathistory.setText(chathistory.getText().substring(0,chathistory.getText().length()-7) + " <br> " + message + "</html>");
            dataOutputStream.writeUTF("messagefrom:" + sourceIp + ";" + message);
            dataOutputStream.flush();
            messagesText.add(message);
            messagesIps.add(sourceIp);
            if(messagesText.size() == 5){
                for (int i=0;i<messagesText.size();i++){
                    filesInterface.Write(messagesText.get(i), messagesIps.get(i));
                }
                messagesText.clear();
                messagesIps.clear();
            }
        } catch (Exception e) {
            DisconectPeer(destinationIp,false);
            super.finalize();
            e.printStackTrace();
        }
    }
    public void reciveMessage(String message) throws Exception {
        chathistory.setText(chathistory.getText().substring(0,chathistory.getText().length()-7) + " <br> " + message + "</html>");
        messagesText.add(message);
        messagesIps.add(sourceIp);
        if(messagesText.size() == 5){
            for (int i=0;i<messagesText.size();i++){
                filesInterface.Write(messagesText.get(i), messagesIps.get(i));
            }
            messagesText.clear();
            messagesIps.clear();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        for (int i=0;i<messagesText.size();i++){
            filesInterface.Write(messagesText.get(i), messagesIps.get(i));
        }
        super.finalize();
    }

}
