package ticketing.ticketing.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.DTO.NoticeRequestDto;
import ticketing.ticketing.Service.NoticeService;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Notice;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<Notice> createNotice(
            @RequestBody NoticeRequestDto requestDto,
            @AuthenticationPrincipal Admin admin
    ) {
        Notice created = noticeService.createNotice(requestDto.getTitle(), requestDto.getContent(), admin);
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
            @RequestBody NoticeRequestDto requestDto
    ) {
        return noticeService.updateNotice(id, requestDto.getTitle(), requestDto.getContent())
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
