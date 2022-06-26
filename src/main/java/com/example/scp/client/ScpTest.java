package com.example.scp.client;

import java.io.File;

public class ScpTest {

    public static void main(String[] args) {
        String ip = "192.168.234.128";
        int port = 22;
        String name = "root";
        String password = "root";
        ScpClient scpClient = ScpClient.getInstance(ip,port,name,password);
//        File f = new File("G:/excel/1530007200125.xls");
//        System.out.println(f);
//        scpClient.uploadFile( f, f.length(),"/home/running/下载/",null);
        scpClient.downloadFile("asdf.log","/etc/usr/local/","D:/download/");
    }

}
