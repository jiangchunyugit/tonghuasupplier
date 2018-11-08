package cn.thinkfree.service.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.ReadingProcessor;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;

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
		fltName = "template.ftl";
//		fltName = "template2.ftl";
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
	public static String savePdf(String flieName, String type, Map<String, Object> root) {
		FileOutputStream out = null;
		Document document = new Document(PageSize.A4, 50, 50, 60, 60);
		String filePath = outPath + flieName+".pdf";
		try {
			out = new FileOutputStream(new File(filePath));//生成pdf

			System.out.println(root);
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
				generatePdf(html,writer,document);
//				XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {

//			document.close();
//			try {
//				out.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
         return filePath;
	}
	private static void generatePdf(String htmlStr,PdfWriter writer,Document document)
			throws IOException, DocumentException {
		//final ServletContext servletContext = getServletContext();
		String CHARSET_NAME ="UTF-8";
//		Document document = new Document(PageSize.A4, 30, 30, 30, 30);
//		document.setMargins(30, 30, 30, 30);
//		PdfWriter writer = PdfWriter.getInstance(document, out);
//		document.open();

		// html内容解析
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(
				new CssAppliersImpl(new XMLWorkerFontProvider() {

				})) {
			@Override
			public HtmlPipelineContext clone()
					throws CloneNotSupportedException {
				HtmlPipelineContext context = super.clone();
				ImageProvider imageProvider = this.getImageProvider();
				context.setImageProvider(imageProvider);
				return context;
			}
		};

		// 图片解析
		htmlContext.setImageProvider(new AbstractImageProvider() {


			@Override
			public String getImageRootPath() {
				return "";
			}

			@Override
			public Image retrieve(String src) {
				if (StringUtils.isEmpty(src)) {
					return null;
				}
				try {
					// String imageFilePath = new File(rootPath, src).toURI().toString();
					Image image = Image.getInstance(src);
					image.setAbsolutePosition(400, 400);
					if (image != null) {
						store(src, image);
						return image;
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return super.retrieve(src);
			}
		});
		htmlContext.setAcceptUnknown(true).autoBookmark(true)
				.setTagFactory(Tags.getHtmlTagProcessorFactory());

		// css解析
		CSSResolver cssResolver = XMLWorkerHelper.getInstance()
				.getDefaultCssResolver(true);
		cssResolver.setFileRetrieve(new FileRetrieve() {
			@Override
			public void processFromStream(InputStream in,
										  ReadingProcessor processor) throws IOException {
				try (InputStreamReader reader = new InputStreamReader(in,
						CHARSET_NAME)) {
					int i = -1;
					while (-1 != (i = reader.read())) {
						processor.process(i);
					}
				} catch (Throwable e) {
				}
			}

			// 解析href
			@Override
			public void processFromHref(String href, ReadingProcessor processor)
					throws IOException {
				// InputStream is = servletContext.getResourceAsStream(href);
				URL url = new URL(href);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5 * 1000);
				InputStream is = conn.getInputStream();

				try (InputStreamReader reader = new InputStreamReader(is,
						CHARSET_NAME)) {
					int i = -1;
					while (-1 != (i = reader.read())) {
						processor.process(i);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext,
				new PdfWriterPipeline(document, writer));
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
				htmlPipeline);
		XMLWorker worker = null;
		worker = new XMLWorker(pipeline, true);
		XMLParser parser = new XMLParser(true, worker,
				Charset.forName(CHARSET_NAME));
		try (InputStream inputStream = new ByteArrayInputStream(
				htmlStr.getBytes())) {
			parser.parse(inputStream, Charset.forName(CHARSET_NAME));
		}
		document.close();
	}

}
