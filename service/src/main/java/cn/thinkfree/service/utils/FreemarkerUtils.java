package cn.thinkfree.service.utils;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.utils.LogUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class FreemarkerUtils {

	static  MyLogger logger = LogUtil.getLogger(FreemarkerUtils.class);

	/**
	 *
	 * @param type
	 *            业务合同类型
	 * @param globalMap
	 *            合同模板中的数据
	 * @return
	 */
	private static String loadFtlHtml(String type, Map<String, Object> globalMap) {
		String fltName = "";
		if (type.equals("0")) {// 设计公司_to_B

			fltName = "design_template.ftl";
		} else if (type.equals("1")) {// 装饰公司_to_B
			fltName = "roadWork_template.ftl";
		} else if (type.equals("2")) {//设计业务方向
			fltName = "interiorDecoration.ftl";
		} else if (type.equals("3")) {//装饰业务方向
			fltName = "constructionContract.ftl";
		}
		else if (type.equals("4")) {//经销商
			fltName = "distributor.ftl";
		}
		//fltName = "template.ftl";

//		InputStream classpath = FreemarkerUtils.class.getClassLoader().getResourceAsStream("templates/"+fltName);
////		URL fileResource = FreemarkerUtils.class.getClassLoader().getResource("templates");
//		Assert.assertNotNull(classpath);
////		String fl = fileResource.getFile();
//		logger.error("File",classpath);
////		ByteArrayInputStream bio = new ByteArrayInputStream(classpath.r)
////		logger.error("Res",fileResource);
//		File baseDir = new File("");
//		if (baseDir == null  || globalMap == null || fltName == null || "".equals(fltName)) {
//			throw new IllegalArgumentException("Directory file 加载模板错误");
//		}

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		try {
			cfg.setClassLoaderForTemplateLoading(FreemarkerUtils.class.getClassLoader(),"templates");
//			cfg.setDirectoryForTemplateLoading(baseDir);
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);// .RETHROW
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
		String filePath =  flieName + ".pdf";
		// 生成pdf
		System.out.println(root);
		String html = loadFtlHtml(type, root);
		try {
			tohtmlPdf(html, filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	public static void tohtmlPdf(String html, String DEST) throws FileNotFoundException, IOException {
		// 装值
		ByteArrayInputStream by = new ByteArrayInputStream(html.getBytes());
		ConverterProperties props = new ConverterProperties();
		DefaultFontProvider defaultFontProvider = new DefaultFontProvider(false, false, false);
    	defaultFontProvider.addFont("/data/font/SimSun.ttf");
		//defaultFontProvider.addFont("d:/data/font/SimSun.ttf");
		props.setFontProvider(defaultFontProvider);
		PdfWriter writer = new PdfWriter(DEST);
		PdfDocument pdf = new PdfDocument(writer);
		pdf.setDefaultPageSize(PageSize.A4);
		PageXofY footer = new PageXofY(pdf);
		pdf.addEventHandler(PdfDocumentEvent.START_PAGE,new Header(""));
		pdf.addEventHandler(PdfDocumentEvent.END_PAGE,footer);
		Document document = HtmlConverter.convertToDocument(by, pdf, props);
		footer.writeTotal(pdf);
		EndPosition endPosition = new EndPosition();
		LineSeparator separator = new LineSeparator(endPosition);
		document.add(separator);
		document.getRenderer().close();
		document.close();
		pdf.close();
	}

	static class EndPosition implements ILineDrawer {

		/** A Y-position. */
		protected float y;

		/**
		 * Gets the Y-position.
		 *
		 * @return the Y-position
		 */
		public float getY() {
			return y;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#draw(com.itextpdf.
		 * kernel.pdf. canvas.PdfCanvas, com.itextpdf.kernel.geom.Rectangle)
		 */
		@Override
		public void draw(PdfCanvas pdfCanvas, Rectangle rect) {
			y = rect.getY();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#getColor()
		 */
		@Override
		public Color getColor() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#getLineWidth()
		 */
		@Override
		public float getLineWidth() {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#setColor(com.itextpdf
		 * .kernel. color.Color)
		 */
		@Override
		public void setColor(Color color) {
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer#setLineWidth(float)
		 */
		@Override
		public void setLineWidth(float lineWidth) {
		}

	}
}
