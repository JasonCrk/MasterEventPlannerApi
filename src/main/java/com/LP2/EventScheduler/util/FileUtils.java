package com.LP2.EventScheduler.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileUtils {

    public File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }

        return tempFile;
    }

    public String getExtensionFile(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String getImageContentType(String extensionFile) {
        if (".png".equalsIgnoreCase(extensionFile)) {
            return "image/png";
        } else if (".jpg".equalsIgnoreCase(extensionFile) || ".jpeg".equalsIgnoreCase(extensionFile)) {
            return "image/jpeg";
        }
        return null;
    }
}
