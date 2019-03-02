import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class tesClass {

    private static ServerSocket serverSocket;
    private static final int projectPortNumber = 4777;

    public tesClass()throws Exception{
        serverSocket = new ServerSocket(projectPortNumber);
        startTalking("");
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
        }catch (Exception ignore){
            return false;
        }
        return false;
    }
}
