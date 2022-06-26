package com.example.scp.client;

import ch.ethz.ssh2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

/**
 * ScpUtil
 */
public class ScpUtil {

    private static final Logger log = LoggerFactory.getLogger(ScpUtil.class);

    /**
     * login 注意：创建远程连接，默认连接端口为22，如果不使用默认，可以使用方法
     *
     * @param
     *
     * @param
     *
     * @author liudz
     * @date 2020/5/11
     * @return 执行结果
     **/
    public static String login() {
        String result = "";
//        String ip = ret.getBody().getWorkerHostPort().substring(0, ret.getBody().getWorkerHostPort().indexOf(":"));
        String ip = "192.168.234.128";
        Connection conn = null;
        Session ss = null;
        String directory = "/etc/usr/local/";
        try {
            conn = new Connection(ip);
            conn.connect();
            boolean b = conn.authenticateWithPassword("root", "root");
            if (!b) {
                throw new IOException("Authentication failed.");
            } else {
                SFTPv3Client sft = new SFTPv3Client(conn);
                List<SFTPv3DirectoryEntry> v = sft.ls(directory);
//                Vector<?> v = (Vector<?>) sft.ls(directory);
                for (int i = 0; i < v.size(); i++) {
                    SFTPv3DirectoryEntry s = new SFTPv3DirectoryEntry();
                    s = (SFTPv3DirectoryEntry) v.get(i);
                    if ("stdout".equals(s.filename)) {
                        ss = conn.openSession();
                        ss.execCommand("cat ".concat("/home/spark/work/"+ s.filename));
                        InputStream is = new StreamGobbler(ss.getStdout());
                        BufferedReader bs = new BufferedReader(new InputStreamReader(is));
                        while (true) {
                            String line = bs.readLine();
                            if (line == null) {
                                break;
                            } else {
                                result += line + "\n";
                            }
                        }
                        bs.close();
                    }
                }
                ss.close();
                conn.close();
            }
        } catch (IOException e) {
            log.error("用户%s密码%s登录服务器%s失败！", ip, "--ERROR--e：" + e.getMessage());
        }
        System.out.print(result);
        return result;
    }

    public static void main(String[] args) {
//        login();
        for(int j = 0; j< 100; j++){
            System.out.println((int)((Math.random()*9+1)*100000));
            System.out.println(Math.random());
        }
        System.out.println(Math.random() * 9 + 1);
        System.out.println(Math.random() * 9);
        System.out.println(Math.random());

    }


}
