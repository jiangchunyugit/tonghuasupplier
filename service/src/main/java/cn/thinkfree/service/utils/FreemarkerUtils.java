package cn.thinkfree.service.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.colors.Color;
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

public class FreemarkerUtils {
	

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
		fltName = "template.ftl";
		URL fileResource = FreemarkerUtils.class.getResource("/templates");
		File baseDir = new File(fileResource.getFile());
		if (baseDir == null || !baseDir.isDirectory() || globalMap == null || fltName == null || "".equals(fltName)) {
			throw new IllegalArgumentException("Directory file 加载模板错误");
		}

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		try {
			cfg.setDirectoryForTemplateLoading(baseDir);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	public static void tohtmlPdf(String html, String DEST) throws FileNotFoundException, IOException {

		// 装值
		ByteArrayInputStream by = new ByteArrayInputStream(html.getBytes());
		ConverterProperties props = new ConverterProperties();
		DefaultFontProvider defaultFontProvider = new DefaultFontProvider(false, false, false);
		URL fileResource = FreemarkerUtils.class.getResource("/templates/font/SimSun.ttf");
		//String  classpath=FreemarkerUtils.class.getClassLoader().getResource("/templates/font/SimSun.ttf").getPath();
        String url= fileResource.getPath();
		defaultFontProvider.addFont(url);
		props.setFontProvider(defaultFontProvider);
		PdfWriter writer = new PdfWriter(DEST);
		PdfDocument pdf = new PdfDocument(writer);
		// pdf.setDefaultPageSize(new PageSize(595, 14400));
		// Document document = HtmlConverter.convertToDocument(new
		// FileInputStream(html), pdf, props);
		pdf.setDefaultPageSize(PageSize.A4);
		Document document = HtmlConverter.convertToDocument(by, pdf, props);

		// 将所有内容在一个页面显示
		EndPosition endPosition = new EndPosition();
		LineSeparator separator = new LineSeparator(endPosition);
		document.add(separator);
		document.getRenderer().close();
		PdfPage page = pdf.getPage(1);
		float y = endPosition.getY() - 36;
		// page.setMediaBox(new Rectangle(0, y, 595, 14400 - y));
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
			this.y = rect.getY();
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
