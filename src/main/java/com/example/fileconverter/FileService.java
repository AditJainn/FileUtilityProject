package com.example.fileconverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service
public class FileService {

    public String convertFile(String fileName) {

        
        return "Converted file: " + fileName;
    }

    
}