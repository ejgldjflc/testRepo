package com.example.scp.client;

import ch.ethz.ssh2.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scp")
public class ScpController {

    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = new Connection("192.168.234.128");
        BufferedInputStream bis = null;
        OutputStream os = null;
        SCPInputStream sis = null;
        response.reset();
        try {
            connection.connect();
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode("asdf.log", "UTF-8"));
            boolean isAuthenticated = connection.authenticateWithPassword("root","root");
            if(isAuthenticated){
                SCPClient scpClient = connection.createSCPClient();
                sis = scpClient.get("/etc/usr/local/asdf.log");
                bis = new BufferedInputStream(sis);
                byte[] buff = new byte[1024];
                os = response.getOutputStream();
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
                System.out.println("download ok");
            }else{
                System.out.println("连接建立失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sis != null) {
                try {
                    sis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }


    @GetMapping("/getLogFileInfo")
    public List<Map<String,Object>> getLogFileInfo() {
        List<Map<String,Object>> result = new ArrayList<>();
        String ip = "192.168.234.128";
        Connection conn = null;
        String directory = "/etc/usr/local/";
        try {
            Map map = new HashMap();
            conn = new Connection(ip);
            conn.connect();
            boolean b = conn.authenticateWithPassword("root", "root");
            if (!b) {
                throw new IOException("Authentication failed.");
            } else {
                SFTPv3Client sft = new SFTPv3Client(conn);
                List<SFTPv3DirectoryEntry> v = sft.ls(directory);
                List<Map> list1 = new ArrayList<>();
                for (int i = 0; i < v.size(); i++) {
                    SFTPv3DirectoryEntry s = new SFTPv3DirectoryEntry();
                    s = (SFTPv3DirectoryEntry) v.get(i);
                    if (s.filename.endsWith(".log")) {
                        Map map1 = new HashMap();
                        map1.put("serverIp", ip);
                        map1.put("fileName", s.filename);
                        map1.put("filePath", directory + s.filename);
                        list1.add(map1);
                    }
                }
                map.put("serverIp", ip);
                map.put("fileInfoList", list1);
                result.add(map);
                conn.close();
            }
        } catch (IOException e) {
        }
        System.out.print(result);
        System.out.println("此方法被调用了。");
        return result;
    }

}
