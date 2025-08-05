package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Notice;
import ticketing.ticketing.application.dto.noticeUpdateRequest.NoticeUpdateRequest;
import ticketing.ticketing.application.dto.noticeCreateRequest.NoticeCreateRequest;
import ticketing.ticketing.application.service.notice.NoticeService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // application.yml upload.path.notice 와 연동
    @Value("${upload.path.notice}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<Notice> createNotice(
            @RequestBody NoticeCreateRequest request,
            @AuthenticationPrincipal Admin admin
    ) {
        Notice created = noticeService.createNotice(
                request.getTitle(),
                request.getContent(),
                admin,
                request.getVisibility(),
                request.getImagePaths()
        );
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        List<Notice> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(@PathVariable Long id) {
        Optional<Notice> notice = noticeService.getNotice(id);
        return notice.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(
            @PathVariable Long id,
            @RequestBody NoticeUpdateRequest request
    ) {
        return noticeService.updateNotice(
                        id,
                        request.getTitle(),
                        request.getContent(),
                        request.getVisibility(),
                        request.getImagePaths()
                ).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        boolean deleted = noticeService.deleteNotice(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // 이미지 업로드 API
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

            // 실제 URL 경로와 매핑되는 부분 (WebConfig에서 설정한 경로와 일치하도록)
            String imagePath = "/upload/notice/" + filename;
            return ResponseEntity.ok(imagePath);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("이미지 업로드 실패: " + e.getMessage());
        }
    }
}
