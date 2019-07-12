package cn.tonghua.service.utils;

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
import sun.misc.BASE64Decoder;

import java.io.*;
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
                InputStream stream = getClass().getClassLoader() .getResourceAsStream("templates/image/logo.png");
                canvas.add(new Image(ImageDataFactory.create(this.input2byte(stream)), pageSize.getLeft()+50 ,pageSize.getTop()-50,500));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
           /* canvas.showTextAligned("                                     ",
                    pageSize.getRight()+10,
                    pageSize.getTop() - 65, TextAlignment.RIGHT);*/


        }





    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }



}
