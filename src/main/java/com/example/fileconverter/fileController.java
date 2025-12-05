package com.example.fileconverter;

import java.nio.charset.StandardCharsets;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class fileController {

    private final FileService fileService;
    private final SecurityService securityService;

    public fileController(FileService fileService, SecurityService securityService) {
        this.fileService = fileService;
        this.securityService = securityService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "This is the name you game me:";

    }

    // for now this will just be for single pdf's ,
    // later we will use different end points for multiple files to maintain clarity
    @PostMapping("/upload")
    public ResponseEntity<byte[]> uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam("onBlankPage") Boolean onBlankPage) {
        try {

            if (!securityService.validFileType(file)) {
                return ResponseEntity.badRequest()
                        .body("Invalid file type. Only JPEG, JPG, and PNG are allowed."
                                .getBytes(StandardCharsets.UTF_8));
            }
            byte[] pdfBytes = fileService.convertImageToPdf(file.getBytes(), onBlankPage);

            if (pdfBytes == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Conversion failed".getBytes(StandardCharsets.UTF_8));
            }

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"converted.pdf\"");

            // Send back as response
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed".getBytes(StandardCharsets.UTF_8));
        }
    }
}