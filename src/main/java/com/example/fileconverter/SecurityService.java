package com.example.fileconverter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

@Service
public class SecurityService {
    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png");

    public boolean isMimeTypeAllowed(String mimeType) {
        return ALLOWED_TYPES.contains(mimeType);
    }

    public boolean isExtensionAllowed(String filename) {
        return filename.endsWith(".jpg") ||
                filename.endsWith(".jpeg") ||
                filename.endsWith(".png");
    }

    public boolean validFileType(MultipartFile file) {

        if (file.isEmpty()) {
            return false;
        }
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename().toLowerCase();

        if (this.isMimeTypeAllowed(contentType) && this.isExtensionAllowed(filename)) {
            return true;
        }
        return false;
    }
}