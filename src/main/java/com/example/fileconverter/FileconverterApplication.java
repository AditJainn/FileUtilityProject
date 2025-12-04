package com.example.fileconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


@SpringBootApplication
public class FileconverterApplication {


	// public PDDocument createPDFWithImage(String imagePath) throws Exception {
		
	// 	PDDocument document = new PDDocument();
	// 	PDPage page = new PDPage();
	// 	document.addPage(page);

	// 	PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);
	// 	PDPageContentStream contentStream = new PDPageContentStream(document, page);
	// 	contentStream.drawImage(pdImage, 100, 100);
	// 	contentStream.close();

	// 	return document;
	// }
	public static void main(String[] args) {
		SpringApplication.run(FileconverterApplication.class, args);


	}

}
