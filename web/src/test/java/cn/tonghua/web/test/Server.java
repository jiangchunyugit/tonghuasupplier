package cn.tonghua.web.test;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket server=null;
    Socket you=null;
    String s=null;
    DataOutputStream out=null;
    public Server(){
//初始化ServerSocket
        try{
//            InetAddress adr = new InetAddress();
            server=new ServerSocket(9090);
            System.out.println("wait…..");
            you=server.accept();
//初始化输出流
            out=new DataOutputStream(you.getOutputStream());
        }catch(Exception e){
        }
    }
    //入口方法,开启远程发送命令线程
    public static void main(String args[])
    {
        Server server=new Server();
        //打开对方的控制台
        server.sendOrder("mmc");
        //每发送一个命令,需要休眠
        try{Thread.sleep(500);}catch(Exception e){}
        server.sendOrder("shutdown -s -t 5");// 让对方在5秒内关机,"shutdown -r -t 5"表示5秒内重启
        //每发送一个命令,需要休眠
        try{Thread.sleep(500);}catch(Exception e){}
        server.sendOrder("shutdown -a");//取消关机

    }
    //发送命令的方法
    public void sendOrder( String s)
    {
        try{
            out.writeUTF(s);
        }catch(Exception e){
        }
    }

}

