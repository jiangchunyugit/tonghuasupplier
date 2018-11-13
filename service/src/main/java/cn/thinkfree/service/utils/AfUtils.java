package cn.thinkfree.service.utils;

import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.vo.AfExportPdfVO;
import cn.thinkfree.database.vo.AfUserDTO;
import cn.thinkfree.service.config.PdfConfig;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.attach.impl.tags.DivTagWorker;
import com.itextpdf.html2pdf.attach.impl.tags.TdTagWorker;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.node.IElementNode;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 审批流工具
 *
 * @author song
 * @version 1.0
 * @date 2018/11/1 18:23
 */

public class AfUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AfUtils.class);

    /**
     * 获取用户信息
     * @param url 外部url
     * @param userId 用户编号
     * @param roleId 角色编号
     * @return 用户信息
     */
    public static AfUserDTO getUserInfo(String url, String userId, String roleId) {
        Map<String, String> requestMap = new HashMap<>(2);
        requestMap.put("userId", userId);
        requestMap.put("roleId", roleId);
        LOGGER.info("获取用户信息，requestMap：{}", JSONUtil.bean2JsonStr(requestMap));
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(url, requestMap);
        LOGGER.info("获取用户信息， httpRespMsg：{}", JSONUtil.bean2JsonStr(httpRespMsg));
        Map responseMap = JSONUtil.json2Bean(httpRespMsg.getContent(), Map.class);
        LOGGER.info("获取用户信息， responseMap：{}", JSONUtil.bean2JsonStr(responseMap));
        Map date = (Map) responseMap.get("data");
        String username = (String) date.get("nickName");
        String headPortrait = (String) date.get("headPortraits");
        String phone = (String) date.get("phone");
        AfUserDTO userDTO = new AfUserDTO();
        userDTO.setUserId(userId);
        userDTO.setUsername(username);
        userDTO.setHeadPortrait(headPortrait);
        userDTO.setPhone(phone);
        return userDTO;
    }

    public static String createPdf(PdfConfig pdfConfig, AfExportPdfVO exportPdfVO, String data) {
        Map<String, Object> dataMap = JSONUtil.json2Bean(data, Map.class);
        Field[] fields = exportPdfVO.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                dataMap.put(field.getName(), field.get(exportPdfVO));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String html = getContent(pdfConfig.getTemplateDir(), exportPdfVO.getConfigNo() + ".ftl", dataMap);

        String exportFileName = createExportFileName(exportPdfVO.getConfigNo());
        createPdf(html, pdfConfig.getExportDir(), exportFileName, pdfConfig.getFontDir(), "");
        return exportFileName;
    }

    private static String createExportFileName(String configNo) {
        return configNo + "-" + Thread.currentThread().getName() + System.currentTimeMillis() + ".pdf";
    }

    public static String getContent(String templateDir, String templateFileName, Object data){
        try{
            Configuration config = new Configuration(Configuration.VERSION_2_3_25);
            config.setDefaultEncoding("UTF-8");

            if (templateDir.startsWith("classpath:")) {
                config.setClassForTemplateLoading(AfUtils.class, templateDir.substring("classpath:".length()));
            } else {
                config.setDirectoryForTemplateLoading(new File(templateDir));
            }
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            config.setLogTemplateExceptions(false);
            Template template = config.getTemplate(templateFileName);
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            writer.flush();
            return writer.toString();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void createPdf(String html, String exportDir, String exportFileName, String fontDir, String resources) {
        try {
            FileOutputStream outputStream;
            if (exportDir.startsWith("classpath:")) {
                URL fileResource = AfUtils.class.getClassLoader().getResource(exportDir.substring("classpath:".length()));
                outputStream = new FileOutputStream(new File(fileResource.getFile(), exportFileName));
            } else {
                outputStream = new FileOutputStream(new File(exportDir, exportFileName));
            }

            WriterProperties writerProperties = new WriterProperties();
            //Add metadata
            writerProperties.addXmpMetadata();

            PdfWriter pdfWriter = new PdfWriter(outputStream, writerProperties);

            PdfDocument pdfDoc = new PdfDocument(pdfWriter);
//            pdfDoc.getCatalog().setLang(new PdfString("zh-CH"));
            // Set the document to be tagged
            pdfDoc.setTagged();
            pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));

            //Set meta tags
            PdfDocumentInfo pdfMetaData = pdfDoc.getDocumentInfo();
            pdfMetaData.setAuthor("Samuel Huylebroeck");
            pdfMetaData.addCreationDate();
            pdfMetaData.getProducer();
            pdfMetaData.setCreator("iText Software");
            pdfMetaData.setKeywords("example, accessibility");
            pdfMetaData.setSubject("PDF accessibility");
            //Title is derived from html

            // pdf conversion
            ConverterProperties converterProperties = new ConverterProperties();
            FontProvider fontProvider = new FontProvider();
            fontProvider.addStandardPdfFonts();
            if (fontDir.startsWith("classpath:")) {
                URL fontResource = AfUtils.class.getClassLoader().getResource(fontDir.substring("classpath:".length()));
                fontProvider.addDirectory(fontResource.getFile());
            } else {
                fontProvider.addDirectory(fontDir);
            }

            converterProperties.setFontProvider(fontProvider);
            converterProperties.setBaseUri("templates/font");

            //Setup custom tagworker factory for better tagging of headers
            DefaultTagWorkerFactory tagWorkerFactory = new AccessibilityTagWorkerFactory();
            converterProperties.setTagWorkerFactory(tagWorkerFactory);

            HtmlConverter.convertToPdf(new ByteArrayInputStream(html.getBytes()), pdfDoc, converterProperties);
            pdfDoc.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class AccessibilityTagWorkerFactory extends DefaultTagWorkerFactory {

        @Override
        public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
            //This can probably replaced with a regex or string pattern
            if(tag.name().equals("h1")){
                return new HeaderTagWorker(tag, context,1);
            }
            if(tag.name().equals("h2")){
                return new HeaderTagWorker(tag, context,2);
            }
            if(tag.name().equals("h3")){
                return new HeaderTagWorker(tag, context,3);
            }
            if(tag.name().equals("h4")){
                return new HeaderTagWorker(tag, context,4);
            }
            if(tag.name().equals("h5")){
                return new HeaderTagWorker(tag, context,5);
            }
            if(tag.name().equals("h6")){
                return new HeaderTagWorker(tag, context,6);
            }

            if(tag.name().equals("th")){
                return new TableHeaderTagWorker(tag,context);
            }

            return null;
        }
    }

    static class HeaderTagWorker extends DivTagWorker {
        private int i;
        public HeaderTagWorker(IElementNode element, ProcessorContext context, int i) {
            super(element, context);
            this.i = i;
        }

        @Override
        public IPropertyContainer getElementResult() {
            Div div =(Div) super.getElementResult();
//            div.setRole(new PdfName("H"+i));
            return super.getElementResult();
        }
    }

    static class TableHeaderTagWorker extends TdTagWorker {
        public TableHeaderTagWorker(IElementNode element, ProcessorContext context) {
            super(element, context);
        }

        @Override
        public IPropertyContainer getElementResult() {
            Cell cell =(Cell) super.getElementResult();
//            cell.setRole(PdfName.TH);
            return super.getElementResult();
        }
    }
}
