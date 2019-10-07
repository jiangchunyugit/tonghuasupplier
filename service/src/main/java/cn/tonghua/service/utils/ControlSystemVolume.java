package cn.tonghua.service.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ControlSystemVolume {

    public static boolean controlSystemVolume(int type) {
        try {

            /**tempFile：vbs 文件
             * vbsMessage：vbs 文件的内容*/
            String vbsMessage = "";
            File tempFile = null;
            Runtime runtime = Runtime.getRuntime();
            switch (type) {
                case 0:
                    tempFile = new File("temp", "volumeMute.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棴\"" : "";
                    break;
                case 1:
                    tempFile = new File("temp", "volumeAdd.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棷\"" : "";
                    break;
                case 2:
                    tempFile = new File("temp", "volumeMinus.vbs");
                    vbsMessage = !tempFile.exists() ? "CreateObject(\"Wscript.Shell\").Sendkeys \"棶\"" : "";
                    break;
                default:
                    return false;
            }
            /**
             * 当3个vbs文件不存在时，则创建它们，应用默认编码为 utf-8 时，创建的 vbs 脚本运行时报错
             * 于是使用 OutputStreamWriter 将 vbs 文件编码改成gbd就正常了
             */
            if (!tempFile.exists() && !vbsMessage.equals("")) {
                if (!tempFile.getParentFile().exists()) {
                    tempFile.getParentFile().mkdirs();
                }
                tempFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "GBK");
                outputStreamWriter.write(vbsMessage);
                outputStreamWriter.flush();
                outputStreamWriter.close();
            }
            runtime.exec("wscript " + tempFile.getAbsolutePath()).waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    return true;
    }
}
