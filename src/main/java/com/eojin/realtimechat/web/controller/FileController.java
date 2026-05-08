package com.eojin.realtimechat.web.controller;

import com.eojin.realtimechat.web.utils.FileUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = FileUtil.saveFile(file);
            return ResponseEntity.ok(new UploadResponse(fileUrl));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Could not upload the file: " + e.getMessage());
        }
    }

    static class UploadResponse {
        public String fileUrl;
        public UploadResponse(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }
}
