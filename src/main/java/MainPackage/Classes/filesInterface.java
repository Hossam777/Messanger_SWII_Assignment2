package MainPackage.Classes;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class filesInterface {

    private static String  name ="history.txt";
    public  static void Write(String ip ,String msg) throws IOException {
        BufferedWriter writer= new BufferedWriter(new FileWriter(name,true));
        writer.write(ip+" :"+msg);
        writer.newLine();
        writer.close();
    }
    public  static void Read(ArrayList<String>ips , ArrayList<String>msg) throws IOException {
        File file = new File(name);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            while(sc.hasNextLine()){
                String str = sc.nextLine();
                String arr[];
                arr = str.split(":");
                ips.add(arr[0]);
                msg.add(arr[1]);

            }

        } catch (IOException  exp) {
            // TODO Auto-generated catch block
            exp.printStackTrace();
        }
        sc.close();
    }
/*
    public static void main(String[] args) throws IOException {

        Write("192.029.3939","Hello");
        Write("192.168.39","How Are you ?");
        ArrayList<String>ips = new ArrayList<String>();
        ArrayList<String>msg = new ArrayList<String>();
        Read(ips,msg);
        System.out.println(ips);
        System.out.println(msg);

    }
*/
}
