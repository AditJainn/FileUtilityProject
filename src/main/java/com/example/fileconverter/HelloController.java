package com.example.fileconverter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HelloController {
    FileService fileService = new FileService();
    
    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg",
            "image/png" );
    @GetMapping("/hello")
    public String hello() {
        return "This is the name you game me:" + fileService.convertFile("Jonh");
        
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // Check MIME type from the request
            String contentType = file.getContentType();
            if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
                return ResponseEntity.badRequest()
                        .body("Invalid file type. Only JPEG, JPG, and PNG are allowed.");
            }

            // OPTIONAL: also check extension
            String filename = file.getOriginalFilename().toLowerCase();
            if (!(filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png"))) {
                return ResponseEntity.badRequest()
                        .body("Invalid file extension. Only .jpg, .jpeg, .png are allowed.");
            }

            return service.processImage(file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Upload failed");
        }
    }
}