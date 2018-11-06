package cn.thinkfree.service.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerUtils {
	/**
	 * 保存路径
	 */
    private static String outPath = "D://11/";
    /**
     * 访问地址
     */
    private static String hostUrl = "http://127.0.0.1:8080/";
    
   
	/**
	 * 
	 * @param type 业务合同类型
	 * @param globalMap 合同模板中的数据
	 * @return
	 */
	private static String loadFtlHtml(String type,Map globalMap){
		String  fltName = "";
		if(type.equals("0")){//设计公司_to_B
			
			fltName = "design_template.ftl";
		}else if(type.equals("1")){//装饰公司_to_B
			fltName = "roadWork_template.ftl";
		}else if(type.equals("2")){
			
		}else if(type.equals("3")){
			
		}
		
		fltName = "template2.ftl";
		URL fileResource = FreemarkerUtils.class.getResource("/templates");
		File baseDir  = new File(fileResource.getFile());
        if(baseDir == null || !baseDir.isDirectory() || globalMap ==null || fltName == null || "".equals(fltName)){
            throw new IllegalArgumentException("Directory file 加载模板错误");
        }

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        try {
            cfg.setDirectoryForTemplateLoading(baseDir);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);//.RETHROW
            cfg.setClassicCompatible(true);
            Template temp = cfg.getTemplate(fltName);

            StringWriter stringWriter = new StringWriter();
            temp.process(globalMap, stringWriter);

            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException("load fail file");
        }
    }

	/**
	 * 
	 * @param flieName pdf url
	 * @param type 合同模板类型
	 * @param root 传入模板的数据
	 */
	public static String savePdf(String flieName, String type, Map<String, List<Map<String, Object>>> root) {
		FileOutputStream out = null;
		Document document = new Document(PageSize.A4, 50, 50, 60, 60);
		String filePath = outPath + flieName+".pdf";
		try {
			out = new FileOutputStream(new File(filePath));//生成pdf


			String html = loadFtlHtml(type, root);
			PdfWriter writer = null;
			try {
				writer = PdfWriter.getInstance(document, out);
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			document.open();
			try {
				XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			
			document.close();
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
         return filePath;
	}
	
    
}
