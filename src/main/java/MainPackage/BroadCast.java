package MainPackage;

import MainPackage.Classes.FilesInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BroadCast {
    private JPanel jPanel;
    private JLabel chathistory;
    private JTextArea messageText;
    private JButton Sendbtn;
    private JButton Generator;
    private JFrame jFrame;
    final String sourceIp;
    ArrayList<DataOutputStream> dataOutputStreams;
    private FilesInterface filesInterface;
    private Thread generator;
    ArrayList<String> messagesText;
    ArrayList<String> messagesIps;

    public BroadCast(String sourceIp, ArrayList<DataOutputStream> dataOutputStreams) {
        jFrame = new JFrame("BroadCast");
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
        messagesText = new ArrayList<String>();
        messagesIps = new ArrayList<String>();
        chathistory.setText("<html> </html>");
        this.sourceIp = sourceIp;
        this.dataOutputStreams = dataOutputStreams;
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
                } catch (Exception e) {
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
        filesInterface = new FilesInterface("BroadCast");

    }

    private void startGenerator() {
        generator.start();
    }
    private void endGenerator() {
        generator.currentThread().interrupt();
    }

    public boolean sendMessage(String message) {
        chathistory.setText(chathistory.getText().substring(0,chathistory.getText().length()-7) + " <br> " + sourceIp + ":" + message + "</html>");
            for (DataOutputStream d : dataOutputStreams) {
                try {
                d.writeUTF("messagefrom:" + sourceIp + ";" + message);
                d.flush();
                messagesText.add(message);
                messagesIps.add(sourceIp);
                if(messagesText.size() == 5){
                    for (int i=0;i<messagesText.size();i++){
                        filesInterface.Write(messagesText.get(i), messagesIps.get(i));
                    }
                    messagesText.clear();
                    messagesIps.clear();
                }
            } catch (IOException e) {
                    dataOutputStreams.remove(dataOutputStreams.indexOf(d));
                }
            }
        return false;
    }
    public void reciveMessage(String ip , String message) throws IOException {
        chathistory.setText(chathistory.getText().substring(0,chathistory.getText().length()-7) + " <br> " + ip + ":" + message + "</html>");
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

    public void addDataOutputStream(DataOutputStream dataOutputStream) {
        dataOutputStreams.add(dataOutputStream);
    }
    public void removeDataOutputStream(DataOutputStream dataOutputStream) {
        dataOutputStreams.remove(dataOutputStreams.indexOf(dataOutputStream));
    }
    public int getDataOutputStreamSize(){
        return dataOutputStreams.size();
    }
}
