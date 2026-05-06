package com.eojin.realtimechat.web.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtil {

    private static final String UPLOAD_DIR = "uploads/";

    public static String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String savedFilename = UUID.randomUUID().toString() + extension;
        Path path = Paths.get(UPLOAD_DIR + savedFilename);
        Files.write(path, file.getBytes());

        return "/uploads/" + savedFilename;
    }
}
