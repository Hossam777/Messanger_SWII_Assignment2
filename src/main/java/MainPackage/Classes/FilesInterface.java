package MainPackage.Classes;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FilesInterface {

    private final String fileName;
    private ArrayList<String> Ips;
    private ArrayList<String> msgs;
    public FilesInterface(String fileName){
        this.fileName = fileName;
        Ips = new ArrayList<String>();
        msgs = new ArrayList<String>();
    }
    public void Write(String ip ,String msg) throws IOException {
        BufferedWriter writer= new BufferedWriter(new FileWriter(fileName,true));
        writer.write(ip+" :"+msg);
        writer.newLine();
        writer.close();
        Ips.add(ip);
        msgs.add(msg);
    }
    public void Read() {
        File file = new File(fileName);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            while(sc.hasNextLine()){
                String str = sc.nextLine();
                String arr[];
                arr = str.split(":");
                Ips.add(arr[0]);
                msgs.add(arr[1]);

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
