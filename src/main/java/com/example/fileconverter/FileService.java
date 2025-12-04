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

    private void writeToPage(PDImageXObject pdImage, PDPage newPage,
            PDDocument document, boolean onBlankPage) throws Exception {
        if (!onBlankPage) // just convert the image to pdf without putting it on a new page
            try (var contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, newPage)) {
                contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
            }
        else {
            float pageWidth = newPage.getMediaBox().getWidth();
            float pageHeight = newPage.getMediaBox().getHeight();
            float imgWidth = pdImage.getWidth();
            float imgHeight = pdImage.getHeight();
            float scale = Math.min(pageWidth / imgWidth, pageHeight / imgHeight);

            float drawWidth = imgWidth * scale;
            float drawHeight = imgHeight * scale;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, newPage)) {
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
            PDPage newPage = new PDPage();
            document.addPage(newPage);

            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "image");
            this.writeToPage(pdImage, newPage, document, onNewPage);
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();

        } catch (Exception e) {
        }

        byte[] pdfBytes = null;
        return pdfBytes;
    }

}