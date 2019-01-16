package cn.thinkfree.service.utils;

import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;


public class Header implements IEventHandler {



        String header;
        String fontPath;
        public Header(String header,String fontPath) {
            this.header = header;
            this.fontPath = fontPath;
        }
        @Override
        public void handleEvent(Event event) {
            //Retrieve document and
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(
                    page.getLastContentStream(), page.getResources(), pdf);
            Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
            canvas.setFontSize(10f);
    // Write text at position
            canvas.setFont("Courier-Bold");
            DefaultFontProvider defaultFontProvider = new DefaultFontProvider(false, false, false);
            defaultFontProvider.addFont(fontPath);
            canvas.setFontProvider(defaultFontProvider);
            try {
                canvas.add(new Image(ImageDataFactory.create("/data/logo.png"), pageSize.getLeft()+50 ,pageSize.getTop()-50,500));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
           /* canvas.showTextAligned("                                     ",
                    pageSize.getRight()+10,
                    pageSize.getTop() - 65, TextAlignment.RIGHT);*/


        }





    /**
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     * @param imgStr base64编码字符串
     * @param path 图片路径-具体到文件
     * @return
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null){
          return false;
        }
            BASE64Decoder decoder = new BASE64Decoder();
            try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
            b[i] += 256;
            }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
            } catch (Exception e) {
            return false;
         }
    }


    }
