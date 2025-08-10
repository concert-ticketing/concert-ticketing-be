package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/banner")
public class BannerController {

    // application.properties에 설정된 업로드 경로
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadBanner(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }

        try {
            // 저장 경로 객체 생성
            Path uploadPath = Paths.get(uploadDir);

            // 경로 존재 여부 확인 후 없으면 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("📁 업로드 폴더 생성: " + uploadPath.toAbsolutePath());
            }

            // 권한 확인
            File dirFile = uploadPath.toFile();
            if (!dirFile.canWrite()) {
                return ResponseEntity.status(500).body("❌ 저장 경로에 쓰기 권한이 없습니다: " + uploadPath);
            }

            // 실제 파일 저장
            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            file.transferTo(filePath.toFile());

            System.out.println("✅ 파일 저장 완료: " + filePath.toAbsolutePath());
            return ResponseEntity.ok("파일 업로드 성공");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 에러: " + e.getMessage());
        }
    }
}
