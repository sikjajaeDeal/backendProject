package likelion.beanBa.backendProject.product.S3.controller;

import likelion.beanBa.backendProject.product.S3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("images") MultipartFile[] files) {
        if (files.length > 15) {
            return ResponseEntity.badRequest().body("Error: Cannot upload more than 15 files at a time.");
        }

        try {
            List<String> uploadedUrls = s3Service.uploadFiles(files); // ✅ URL 받기
            return ResponseEntity.ok(uploadedUrls); // ✅ URL 리스트 응답
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("File upload failed: " + e.getMessage());
        }
    }
}
