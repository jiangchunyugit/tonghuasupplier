package cn.thinkfree.service.utils;

import java.io.File;

import org.jodconverter.DocumentConverter;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 把doc 转换为pdf文件
 * @author lvqidong
 *
 */
public class PdfUtils {

	
	@Autowired
	private DocumentConverter documentConverter;
	
	 /**
	  * 
	  * @param dataMap
	  * @param templateName
	  * @param filePath
	  * @param fileName
	  */
	 public   void createPdf(File sourceFile,File targetFile){
		 
		 try {
			 documentConverter.convert(sourceFile).to(targetFile).execute();
			} catch (OfficeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 
	 }
	 
	 public static void main(String[] args) {
		 File sourceFile = new File("D:/11/居然设计家装饰公司平台入驻合作合同模板.docx");
			File targetFile = new File("D:/11/测试.pdf");
			// 具体转换方法，参数是java.io.File 
			PdfUtils s= new PdfUtils();
			s.createPdf(sourceFile, targetFile);
			
	}
	
		
}
