package cn.tonghua.core.utils;

import cn.tonghua.core.base.MyLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author jiangchunyu 上传，下载，解压
 * @date 20190704 （同华整理）
 */
@Component
public class WebFileUtil {

    /**
     * 文件上传路径
     */
    private static String uploadDir;

    /**
     * 文件服务器路径
     */
    private static String publicURL;

    /**
     * 配置文件获取本地地址
     * @param publicURL
     */
    @Value("${custom.file.publicURL}")
    public  void setPublicURL(String publicURL) {
        WebFileUtil.publicURL = publicURL;
    }

    /**
     * 配置文件获取资源服务器地址
     * @param up
     */
    @Value("${server.file.uploadDir}")
    public  void setUploadDir(String up) {
        WebFileUtil.uploadDir = up;
    }

    static MyLogger logger = LogUtil.getLogger(WebFileUtil.class);

    public static String fileCopy(String target, MultipartFile source){
        String timestamp=new SimpleDateFormat("_yyyyMMddhhmmss").format(new Date());
        String FileName = makePath(false,target, source.getOriginalFilename(),timestamp);
        try (InputStream in = new BufferedInputStream(source.getInputStream());
             ReadableByteChannel rc = Channels.newChannel(in);
             FileOutputStream out = new FileOutputStream(new File(FileName));
        ){

            FileChannel outChannel = out.getChannel();
            outChannel.transferFrom(rc, 0, source.getSize());
        } catch ( IOException e) {
            logger.error("The FileCopy Is Error :{}",e);
        }
        // 返回服务器位置
        String path = escapePath(makePath(true, target,source.getOriginalFilename(),timestamp));
        return path;
    }

    /**
     * @deprecated 服务器地址格式更改
     * @param makePath
     * @return
     */
    private static String escapePath(String makePath) {
        if(StringUtils.isNotBlank(makePath) && makePath.length() > 7){
            makePath=makePath.replaceAll("\\\\", "/");
        }

        logger.error(makePath);
        return  makePath.replace("http:/","http://");
    }



    public static boolean fileCopy(){
        Path path = Paths.get("");
        Path target = Paths.get("");
        try {
            Files.copy(path, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void fileCopy2(){

        try(FileInputStream in = new FileInputStream(new File(""));
            FileOutputStream out = new FileOutputStream(new File(""));
        ){

            FileChannel fin = in.getChannel();
            FileChannel fon = out.getChannel();
            ByteBuffer bytebuffer = ByteBuffer.allocateDirect(1025);
            int bytesCount;
            while ((bytesCount = fin.read(bytebuffer)) > 0) {
                bytebuffer.flip();
                fon.write(bytebuffer);
                bytebuffer.clear();
            }
        } catch (IOException e) {
            logger.error("The FileCopy2 Error :{}",e);
        }
    }

    /**
     * 文件解压方法
     * @param file    源文件 --来自spring mvc 封装后的对象
     * @param tmpDir  临时目录  --用于命名
     * @return        所有文件路径
     */
    public static List<String> desZipFile(MultipartFile file, String tmpDir){
        List<String> fileUrlList =new ArrayList<String>();
        try(ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
            InputStream bis = new BufferedInputStream(zipInputStream);
            ReadableByteChannel rc = Channels.newChannel(bis);
        ){
            FileOutputStream bos =null;

            ZipEntry zipEntry =null;
            while((zipEntry = zipInputStream.getNextEntry())!=null){
                File  tmpFile = new File(makePath(false,tmpDir,zipEntry.getName()));
                if(!tmpFile .exists() && zipEntry.isDirectory()){
                    tmpFile.mkdir();
                    continue;
                }
                bos =  new FileOutputStream(tmpFile);
                FileChannel bosc = bos.getChannel();
                bosc.transferFrom(rc, 0, file.getSize());
                fileUrlList.add(makePath(true,tmpDir, zipEntry.getName()).replaceAll("\\\\", "/"));
            }
        } catch (IOException e) {
            logger.error("IO Exception");
        }

        return fileUrlList;
    }

    /**
     * 名字拼装
     * @param tmpDir  目录-业务名
     * @param name    文件名
     * @return
     */
    private static String makePath(boolean isShow,String tmpDir, String ... name) {

        // 拼接目录
        String tmpPath =
                isShow ?
                        publicURL
                                +new SimpleDateFormat("yyyyMMdd").format(new Date())
                                +File.separator+tmpDir
                        :
                               uploadDir
                                +new SimpleDateFormat("yyyyMMdd").format(new Date())
                                +File.separator+tmpDir;

        File pathDir= new File(tmpPath );
        if(!isShow  && !pathDir.exists()){
            pathDir.mkdirs();
        }
        String tmpName =null;
        // 拼接文件名称（日期+原名称）
        if(name.length == 2){
            int suf = name[0].indexOf(".");
            tmpName = suf > -1 ? name[0].substring(0,suf)+name[1]+name[0].substring(suf):name[0];
        }else{
            tmpName =name[0];
        }
        return    pathDir.getPath()+File.separator+tmpName;
    }

}
