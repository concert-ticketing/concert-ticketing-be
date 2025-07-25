package ticketing.ticketing.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Notice;
import ticketing.ticketing.Service.NoticeService;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<Notice> createNotice(
            @RequestParam String title,
            @RequestParam String content,
            @AuthenticationPrincipal Admin admin
    ) {
        Notice created = noticeService.createNotice(title, content, admin);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        List<Notice> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(@PathVariable Long id) {
        return noticeService.getNotice(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content
    ) {
        return noticeService.updateNotice(id, title, content)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        boolean deleted = noticeService.deleteNotice(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
