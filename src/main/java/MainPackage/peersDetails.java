package MainPackage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class peersDetails  {

    private JPanel jPanel;
    private JList ipsList;
    private JButton callbtn;
    private static JFrame jFrame;
    private static ServerSocket serverSocket;
    private ArrayList<String> onlineIps;
    private HashMap<String,ChatForm> mysessions;
    private final String myIp;

    public peersDetails(final String myip, ArrayList<String> onlineips) throws Exception{
        myIp = myip;
        jFrame = new JFrame("Server");
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
        serverSocket = new ServerSocket(4777);
        onlineIps = onlineips;
        mysessions = new HashMap<String, ChatForm>();
        if(onlineIps != null){
            ipsList.setListData(onlineIps.toArray());
        }
        callbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //call ip address
                int i = ipsList.getSelectedIndex();
                if(i != -1){
                    try {
                        if(!mysessions.containsKey(onlineIps.get(i)))
                            mysessions.put(onlineIps.get(i),new ChatForm(myip,onlineIps.get(i)));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void listen(){
        Thread listeningThread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true){
                        Socket socket = serverSocket.accept();
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        String message = "";
                        message = dataInputStream.readUTF();
                        String header = message.substring(0,message.indexOf(':'));
                        //handle message
                        if(header.equals("newip")){
                            onlineIps.add(message.substring(message.indexOf(':')+1));
                            ipsList.setListData(onlineIps.toArray());
                        }else if(header.equals("messagefrom")){
                            String srcIp = message.substring(message.indexOf(':')+1,message.indexOf(';'));
                            String messageText = message.substring(message.indexOf(';')+1);
                            ///send message to chat
                            if(mysessions.containsKey(srcIp)){
                                mysessions.get(srcIp).reciveMessage(messageText);
                            }else{
                                mysessions.put(srcIp,new ChatForm(myIp,srcIp));
                                mysessions.get(srcIp).reciveMessage(messageText);
                            }
                        }else{
                            ///message corrupted

                        }
                    }
                }catch (Exception ignore){}
            }
        });
        listeningThread.start();
    }
}
