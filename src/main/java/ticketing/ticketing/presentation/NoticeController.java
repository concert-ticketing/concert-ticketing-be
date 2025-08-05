package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Notice;
import ticketing.ticketing.application.dto.noticeUpdateRequest.NoticeUpdateRequest;
import ticketing.ticketing.application.dto.noticeCreateRequest.NoticeCreateRequest;
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
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
