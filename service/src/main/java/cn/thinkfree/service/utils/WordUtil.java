package cn.thinkfree.service.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Template;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


@Component
public class WordUtil {


	/**
     * 文件上传路径
     */
    private static String uploadDir;
    /**
     * 文件服务器路径
     */
    private static String publicURL;

    @Value("${custom.file.publicURL}")
    public  void setPublicURL(String publicURL) {
    	WordUtil.publicURL = publicURL;
    }

    @Value("${server.file.uploadDir}")
    public  void setUploadDir(String up) {
    	WordUtil.uploadDir = up;
    }
 
    /**
     * 生成word文件
     * @param dataMap word中需要展示的动态数据，用map集合来保存
     * @param templateName word模板名称，例如：test.ftl
     * @param filePath 文件生成的目标路径，例如：D:/wordFile/
     * @param fileName 生成的文件名称，例如：test.doc
     */
    @SuppressWarnings("unchecked")
    public static void createWord(FreeMarkerConfigurer configurer,Map dataMap,String templateName,String filePath,String fileName){
        try {
            /*//创建配置实例
            Configuration configuration = new Configuration();
 
            //设置编码
            configuration.setDefaultEncoding("UTF-8");
 
            //ftl模板文件
            //configuration.setClassForTemplateLoading(WordUtil.class,"/templates/");
            configuration.setClassForTemplateLoading(WordUtil.class, "/template/");
            //获取模板
            Template template = configuration.getTemplate(templateName);*/

            Template template = configurer.getConfiguration().getTemplate(templateName);
            FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap);
            //输出文件
            File outFile = new File("/usr/local/data/"+File.separator+fileName);
 
            //如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()){
                outFile.getParentFile().mkdirs();
            }
 
            //将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
 
 
            //生成文件
            template.process(dataMap, out);
 
            //关闭流
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}