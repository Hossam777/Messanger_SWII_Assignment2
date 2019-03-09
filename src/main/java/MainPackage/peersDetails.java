package MainPackage;

import MainPackage.Classes.TrackerPostIp;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static MainPackage.MainForm.tracker;

public class peersDetails  {

    private JPanel jPanel;
    private static JList ipsList;
    private JButton callbtn;
    private static JFrame jFrame;
    private static ServerSocket serverSocket;
    private static ArrayList<String> onlineIps;
    private static HashMap<String,ChatForm> mysessions;
    private static String myIp = null;
    private static BroadCast broadCast;
    private static HashMap<String,Socket> sockets ;
    private static HashMap<String,DataInputStream> dataInputStreams ;
    private static HashMap<String,DataOutputStream> dataOutputStreams ;

    public peersDetails(final String myip) throws Exception{
        broadCast = null;
        this.myIp = myip;
        jFrame = new JFrame("Server");
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
        serverSocket = new ServerSocket(4777);
        sockets = new HashMap<String, Socket>();
        dataInputStreams = new HashMap<String, DataInputStream>();
        dataOutputStreams = new HashMap<String, DataOutputStream>();
        this.onlineIps = new ArrayList<String>();
        mysessions = new HashMap<String, ChatForm>();
        callbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //call ip address
                int i = ipsList.getSelectedIndex();
                if(i != -1){
                    try {
                        if(!mysessions.containsKey(onlineIps.get(i)))
                            if( i == 0){
                                if(broadCast == null){
                                    Collection<DataOutputStream> values = dataOutputStreams.values();
                                    ArrayList<DataOutputStream>dataOutputStreamsarray = new ArrayList<DataOutputStream>(values);
                                    new BroadCast(myip,dataOutputStreamsarray);
                                }
                            }
                            else
                                mysessions.put(onlineIps.get(i),new ChatForm(myip,onlineIps.get(i),dataOutputStreams.get(onlineIps.get(i))));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        listen();
    }
    public peersDetails(final String myip, ArrayList<String> onlineips) throws Exception{
        broadCast = null;
        this.myIp = myip;
        jFrame = new JFrame("Server");
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
        serverSocket = new ServerSocket(4777);
        this.sockets = new HashMap<String, Socket>();
        this.dataInputStreams = new HashMap<String, DataInputStream>();
        this.dataOutputStreams = new HashMap<String, DataOutputStream>();
        this.onlineIps = onlineips;
        mysessions = new HashMap<String, ChatForm>();
        ipsList.setListData(onlineIps.toArray());
        setUp();
        callbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //call ip address
                int i = ipsList.getSelectedIndex();
                if(i != -1){
                    try {
                        if(!mysessions.containsKey(onlineIps.get(i))){
                            if( i == 0){
                                if(broadCast == null){
                                    Collection<DataOutputStream> values = dataOutputStreams.values();
                                    ArrayList<DataOutputStream>dataOutputStreamsarray = new ArrayList<DataOutputStream>(values);
                                    new BroadCast(myip,dataOutputStreamsarray);
                                }
                            }
                            else
                                mysessions.put(onlineIps.get(i),new ChatForm(myip,onlineIps.get(i),dataOutputStreams.get(onlineIps.get(i))));
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        listen();
    }

    private void setUp() throws Exception{
        for(int i = 1;i<onlineIps.size();i++){
            Socket socket = new Socket(onlineIps.get(i),4777);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            sockets.put(onlineIps.get(i),socket);
            dataInputStreams.put(onlineIps.get(i),dataInputStream);
            dataOutputStreams.put(onlineIps.get(i),dataOutputStream);
            starSocketListining(onlineIps.get(i),dataInputStream);
        }
    }

    private void starSocketListining(final String ip, final DataInputStream dataInputStream){
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String message ,header ;
                while(true){
                    try {
                        message = dataInputStream.readUTF();
                        if(message.equals("close"))
                        {
                            if(broadCast != null) {
                                broadCast.removeDataOutputStream(dataOutputStreams.get(ip));
                                if(broadCast.getDataOutputStreamSize() == 0){
                                    broadCast.finalize();
                                    broadCast = null;
                                }
                            }
                            mysessions.get(ip).finalize();
                            mysessions.remove(ip);
                            dataInputStream.close();
                            dataInputStreams.remove(ip);
                            dataOutputStreams.get(ip).close();
                            dataOutputStreams.remove(ip);
                            sockets.get(ip).close();
                            sockets.remove(ip);
                            break;
                        }
                        header = message.substring(0,message.indexOf(':'));
                        //handle message
                        if(header.equals("messagefrom")){
                            String srcIp = message.substring(message.indexOf(':')+1,message.indexOf(';'));
                            String messageText = message.substring(message.indexOf(';')+1);
                            ///send message to chat
                            if(srcIp.equals("BroadCast")){
                                if(broadCast == null){
                                    Collection<DataOutputStream> values = dataOutputStreams.values();
                                    ArrayList<DataOutputStream>dataOutputStreamsarray = new ArrayList<DataOutputStream>(values);
                                    broadCast = new BroadCast(srcIp, dataOutputStreamsarray);
                                    broadCast.reciveMessage(messageText.substring(0,messageText.indexOf(':')),
                                            messageText.substring(messageText.indexOf(':' + 1)));
                                }else{
                                    broadCast.reciveMessage(messageText.substring(0,messageText.indexOf(':')),
                                            messageText.substring(messageText.indexOf(':' + 1)));
                                }
                            }else if(mysessions.containsKey(srcIp)){
                                mysessions.get(srcIp).reciveMessage(messageText);
                            }else{
                                DataOutputStream dataOutputStream = dataOutputStreams.get(srcIp);
                                mysessions.put(srcIp ,new ChatForm(myIp, srcIp, dataOutputStream));
                                mysessions.get(srcIp).reciveMessage(messageText);
                            }
                        }else{
                            ///message corrupted
                            System.out.println("message corrupted");
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
    private void listen(){
        Thread listeningThread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true){
                        Socket socket = serverSocket.accept();
                        String remoteip = socket.getRemoteSocketAddress().toString();
                        remoteip = remoteip.substring(1,remoteip.indexOf(":"));
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        DataInputStream dataInputStream= new DataInputStream(socket.getInputStream());
                        sockets.put(remoteip,socket);
                        dataOutputStreams.put(remoteip,dataOutputStream);
                        dataInputStreams.put(remoteip,dataInputStream);
                        starSocketListining(remoteip,dataInputStream);
                        if(broadCast != null)
                            broadCast.addDataOutputStream(dataOutputStream);
                        if(onlineIps.size() == 0)
                            onlineIps.add("BroadCast");
                        onlineIps.add(remoteip);
                        ipsList.setListData(onlineIps.toArray());
                    }
                }catch (Exception ignore){
                    ignore.printStackTrace();
                }
            }
        });
        listeningThread.start();
    }

    @Override
    protected void finalize() throws Throwable {
        DisconectPeer(myIp,true);
        for ( String key : dataOutputStreams.keySet() ) {
            dataOutputStreams.get(key).writeUTF("close");
            dataOutputStreams.get(key).flush();
        }
        super.finalize();
    }

    public static void DisconectPeer(String ip, boolean me) throws Throwable {
        if(!me){
            onlineIps.remove(onlineIps.indexOf(ip));
            dataOutputStreams.get(ip).close();
            dataOutputStreams.remove(ip);
            dataInputStreams.get(ip).close();
            dataInputStreams.remove(ip);
            sockets.get(ip).close();
            sockets.remove(ip);
            ipsList.setListData(onlineIps.toArray());
        }
        tracker.deleteIp(ip, new TrackerPostIp.CallBack() {
            public void completed(JSONArray jsonArray) throws Throwable {

            }

            public void failed() {

            }
        });
    }
}
