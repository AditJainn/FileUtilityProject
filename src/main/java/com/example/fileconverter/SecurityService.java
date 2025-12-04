package com.example.fileconverter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityService {

    public String convertFile(String fileName) {
        return "Converted file: " + fileName;
    }

    public Boolean isFileSecure(MultipartFile file) {

          if (file.isEmpty()) {
                return false;
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

        return "Image processed successfully";
        // Implement image processing logic here

    }

    
}