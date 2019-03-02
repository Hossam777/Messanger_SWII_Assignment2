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
    private final String myip;

    public peersDetails(final String myip) throws Exception{
        this.myip = myip;
        jFrame = new JFrame("Server");
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
        serverSocket = new ServerSocket(4777);
        onlineIps = new ArrayList<String>();
        mysessions = new HashMap<String, ChatForm>();
        callbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //call ip address
                int i = ipsList.getSelectedIndex();
                if(i != -1){
                    try {
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
                    Socket socket = serverSocket.accept();
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String message = "";
                    while (true){
                        message = dataInputStream.readUTF();
                        //handle message
                        if(message.contains("newip=")){

                        }else if(message.contains("messagefrom")){

                        }else{

                        }
                    }
                }catch (Exception ignore){}
            }
        });
        listeningThread.start();
    }
}
