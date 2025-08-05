package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Notice;
import ticketing.ticketing.application.service.notice.NoticeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<Notice> createNotice(
            @RequestBody NoticeCreateRequest request,
            @AuthenticationPrincipal Admin admin
    ) {
        Notice created = noticeService.createNotice(
                request.title(),
                request.content(),
                admin,
                request.imagePaths()
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
                        request.title(),
                        request.content(),
                        request.imagePaths()
                ).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        boolean deleted = noticeService.deleteNotice(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    public record NoticeCreateRequest(String title, String content, List<String> imagePaths) {}

    public record NoticeUpdateRequest(String title, String content, List<String> imagePaths) {}
}
