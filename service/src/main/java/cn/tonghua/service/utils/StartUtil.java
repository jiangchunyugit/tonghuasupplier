package cn.tonghua.service.utils;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class StartUtil {

    /**
     * main方法，发送UDP广播，实现远程开机，目标计算机的MAC地址为：‎44032CEDC37B
     */
    public static void startMachine(String ipadress, String mask) throws IOException {

        DatagramSocket ds = null;

        try {
            ds = new DatagramSocket(9999);  //指定自己的port
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
        byte a = Integer.valueOf(0x18).byteValue();
        byte b = Integer.valueOf(0x31).byteValue();
        byte c = Integer.valueOf(0xBF).byteValue();
        byte d = Integer.valueOf(0xDE).byteValue();
        byte e = Integer.valueOf(0x89).byteValue();
        byte f = Integer.valueOf(0x53).byteValue();
        byte g = Integer.valueOf(0xff).byteValue();
        byte[] buf= {g,g,g,g,g,g,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f,a,b,c,d,e,f};
        //魔术包数据
        String magicPacage = "0xFFFFFFFFFFFF" +
                mask + mask + mask + mask +
                mask + mask + mask + mask +
                mask + mask + mask + mask +
                mask + mask + mask + mask;
        //转换为2进制的魔术包数据
        byte[] command = hexToBinary(magicPacage);

        // 孔博测试
        byte dateKong = Integer.valueOf(100).byteValue();
        byte[] bufK = {dateKong};


        if (buf == command) {

            System.out.print("zhe ge zen m wan ");
        }

        String MODSET = "CTR_MEDIA:0[@窗口ID]";
        //广播魔术包

            //1.获取ip地址
            InetAddress address = InetAddress.getByName("172.17.23.141");
            //2.获取广播socket
//            MulticastSocket socket = new MulticastSocket(port);
            //3.封装数据包
            /*public DatagramPacket(byte[] buf,int length
             *      ,InetAddress address
             *      ,int port)
             * buf：缓存的命令
             * length：每次发送的数据字节数，该值必须小于等于buf的大小
             * address：广播地址
             * port：广播端口
             */
//            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 5050);
        int len = MODSET.length();
        DatagramPacket packet = new DatagramPacket(bufK,bufK.length, address, 5050);
            //4.发送数据
            ds.send(packet);
            //5.关闭socket
            ds.close();
    }

    /**
     * 将16进制字符串转换为用byte数组表示的二进制形式
     * @param hexString：16进制字符串
     * @return：用byte数组表示的十六进制数
     */
    private static byte[] hexToBinary(String hexString){
        //1.定义变量：用于存储转换结果的数组
        byte[] result = new byte[hexString.length()];

        //2.去除字符串中的16进制标识"0X"并将所有字母转换为大写
        hexString = hexString.toUpperCase().replace("0X", "");

        //3.开始转换
        //3.1.定义两个临时存储数据的变量
        char tmp1 = '0';
        char tmp2 = '0';
        //3.2.开始转换，将每两个十六进制数放进一个byte变量中
        for(int i = 0; i < hexString.length(); i += 2){
            result[i/2] = (byte)((hexToDec(tmp1)<<4)|(hexToDec(tmp2)));
        }
        return result;
    }

    /**
     * 用于将16进制的单个字符映射到10进制的方法
     * @param c：16进制数的一个字符
     * @return：对应的十进制数
     */
    private static byte hexToDec(char c){
        return (byte)"0123456789ABCDEF".indexOf(c);
    }
}
