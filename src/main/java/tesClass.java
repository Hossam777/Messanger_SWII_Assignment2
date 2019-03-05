import MainPackage.Classes.TrackerPostIp;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Future;

public class tesClass {

    private static ServerSocket serverSocket;
    private static final int projectPortNumber = 4777;


    public static void main(String[] args)throws Exception{
        //serverSocket = new ServerSocket(projectPortNumber);
        //startTalking("");
        //InetAddress ip=java.net.InetAddress.getLocalHost();
        /*TrackerPostIp trackerPostIp = new TrackerPostIp();
        trackerPostIp.test(ip.getHostAddress(), new TrackerPostIp.CallBack() {
            public void completed(JSONArray jsonArray) throws Exception {

            }

            public void completed(String ip) throws Exception {
                System.out.println(true + ip);
                Socket socket = new Socket(ip,6666);
                DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
                dout.writeUTF("ekjhgfds Software 2");
                dout.flush();
                dout.close();
                socket.close();

                Socket socketO = new Socket(ip,6666);
                DataOutputStream doutO=new DataOutputStream(socketO.getOutputStream());
                doutO.writeUTF("ekjhgfds Software 3");
                doutO.flush();
                doutO.close();
                socketO.close();
            }

            public void failed() {

            }
        });*/

        Socket socket = new Socket("192.168.43.228",9999);
        DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
        dout.writeUTF("Eh el 5ra da");
        dout.flush();
        dout.close();
        socket.close();

    }
    private static boolean startTalking(String ipaddress){
        try {
            final Socket socket = new Socket(ipaddress,projectPortNumber);
            Thread Talk = new Thread(new Runnable() {
                public void run() {
                    try {
                        DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
                        dout.writeUTF("Hello Server");
                        dout.flush();
                        dout.close();
                        socket.close();
                    }catch (Exception ignore){}
                }
            });
            Talk.start();
            ServerSocket ss=new ServerSocket(6666);
            Socket s=ss.accept();//establishes connection
            DataInputStream dis=new DataInputStream(s.getInputStream());
            String  str=(String)dis.readUTF();
            System.out.println("message= "+str);

            InetAddress ip=java.net.InetAddress.getLocalHost();
            //System.out.println("IP Address: "+ip.getHostAddress());
        }catch (Exception ignore){
            return false;
        }
        return false;
    }
}
