package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.application.dto.noticeCreateRequest.NoticeCreateRequest;
import ticketing.ticketing.application.dto.noticeUpdateRequest.NoticeUpdateRequest;
import ticketing.ticketing.application.dto.noticeResponseDto.NoticeResponse;
import ticketing.ticketing.application.service.notice.NoticeService;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Notice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Value("${upload.path.notice}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<NoticeResponse> createNotice(
            @RequestBody NoticeCreateRequest request,
            @AuthenticationPrincipal Admin admin
    ) {
        NoticeResponse created = noticeService.createNotice(request, admin);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(@PathVariable Long id) {
        return noticeService.getNotice(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponse> updateNotice(
            @PathVariable Long id,
            @RequestBody NoticeUpdateRequest request
    ) {
        return noticeService.updateNotice(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        boolean deleted = noticeService.deleteNotice(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filepath = Paths.get(uploadDir, filename);
            Files.copy(file.getInputStream(), filepath);

            String imagePath = "/upload/notice/" + filename;
            return ResponseEntity.ok(imagePath);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("이미지 업로드 실패: " + e.getMessage());
        }
    }
}
