package cn.thinkfree.controller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.thinkfree.service.utils.WordUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
public class FreeMarkerTest {

	
//	public static void main(String[] args) {    
//	    Configuration con=new Configuration();
//	    try {
//	        con.setDirectoryForTemplateLoading(new File("d://11"));//指定加载模板的位置
//	        con.setObjectWrapper(new DefaultObjectWrapper());//指定生产模板的方式
//	        con.setDefaultEncoding("utf-8");//设置模板读取的编码方式，用于处理乱码
//	        Template template = con.getTemplate("NewFile.xml");//模板文件，可以是xml,ftl,html
//	        System.out.println(template.getEncoding());
//	        template.setEncoding("utf-8");//设置写入模板的编码方式        
//	        Map root=new HashMap();//data数据
//	    
//	        List reportresult =new ArrayList();
//	        Map rep=new HashMap();
//	        rep.put("test1", "统计设备一");
//	        rep.put("test2", "192.168.6.64");
//	        rep.put("test3", "30");
//	        rep.put("test4", "20");
//	        rep.put("test5", "10");
//	        reportresult.add(rep);
//	        root.put("reportresult", reportresult);
//	        
//	        Writer out = new BufferedWriter(new OutputStreamWriter( new  FileOutputStream(new File("d://11//s1.doc" +
//	                "")),"utf-8"));//生产文件输出流 
//	        template.process(root, out);//将模板写到文件中
//	        out.flush();
//	    } catch (IOException e) {
//	        // TODO Auto-generated catch block
//	        e.printStackTrace();
//	    } catch (TemplateException e) {
//	        // TODO Auto-generated catch block
//	        e.printStackTrace();
//	    }
//	}
//	
	
	public static void main(String[] args) {
		 Map root=new HashMap();//data数据
		    
	        List reportresult =new ArrayList();
	        Map rep=new HashMap();
	        rep.put("test1", "统计设备一");
	        rep.put("test2", "192.168.6.64");
	        rep.put("test3", "30");
	        rep.put("test4", "20");
	        rep.put("test5", "10");
	        reportresult.add(rep);
	        root.put("reportresult", reportresult);
		WordUtil.createWord(root, "NewFile.xml", "d://11", "s2.doc");
	}
	
}
