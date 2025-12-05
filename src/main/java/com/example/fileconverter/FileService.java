// https://javadoc.io/doc/org.apache.pdfbox/pdfbox/latest/index.html
package com.example.fileconverter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class FileService {
    // // heler overloaded method incase we dont want to create a page in previous
    // method
    // private void writeToPage(PDImageXObject pdImage,
    // PDDocument document, boolean onBlankPage) throws Exception {
    // PDPage newPage = new PDPage();
    // this.writeToPage(pdImage, newPage,document, onBlankPage);
    // }

    private void writeToPage(PDImageXObject pdImage,
            PDDocument document, boolean onBlankPage) throws Exception {
        float imgWidth = pdImage.getWidth();
        float imgHeight = pdImage.getHeight();
        if (!onBlankPage) { // just convert the image to pdf without putting it on a new page
            PDPage page = new PDPage(new PDRectangle(imgWidth, imgHeight));
            document.addPage(page);
            try (var contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page)) {
                contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
            }
        } else {
            PDPage page = new PDPage();
            document.addPage(page);
            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();

            float scale = Math.min(pageWidth / imgWidth, pageHeight / imgHeight);

            float drawWidth = imgWidth * scale;
            float drawHeight = imgHeight * scale;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.drawImage(
                        pdImage,
                        (pageWidth - drawWidth) / 2,
                        (pageHeight - drawHeight) / 2,
                        drawWidth,
                        drawHeight);
            }
        }
    }

    public byte[] convertImageToPdf(byte[] imageBytes, boolean onNewPage) throws Exception {

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        if (image == null) {
            throw new IllegalArgumentException("Image could not be read");
        }
        try (PDDocument document = new PDDocument()) {

            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "image");
            this.writeToPage(pdImage, document, onNewPage);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();

        } catch (Exception e) {
        }

        byte[] pdfBytes = null;
        return pdfBytes;
    }

}