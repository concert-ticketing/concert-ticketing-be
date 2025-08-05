package ticketing.ticketing.application.service.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Notice;
import ticketing.ticketing.domain.entity.NoticeImage;
import ticketing.ticketing.domain.enums.NoticeVisibility;
import ticketing.ticketing.infrastructure.repository.notice.NoticeImageRepository;
import ticketing.ticketing.infrastructure.repository.notice.NoticeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeImageRepository noticeImageRepository;

    // 프로퍼티에서 notice 업로드 경로 주입
    @Value("${upload.path.notice}")
    private String uploadPath;

    @Transactional
    public Notice createNotice(String title, String content, Admin admin, NoticeVisibility visibility, List<String> imagePaths) {
        Notice notice = Notice.create(title, content, admin, visibility);
        Notice savedNotice = noticeRepository.save(notice);

        if (imagePaths != null && !imagePaths.isEmpty()) {
            for (String path : imagePaths) {
                // 이미지 경로를 저장할 때 uploadPath를 활용하는 방식으로 필요시 변환 가능
                NoticeImage image = NoticeImage.of(savedNotice, path);
                noticeImageRepository.save(image);
            }
        }

        return savedNotice;
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Optional<Notice> getNotice(Long id) {
        return noticeRepository.findById(id);
    }

    public List<NoticeImage> getImagesByNotice(Notice notice) {
        return noticeImageRepository.findAllByNotice(notice);
    }

    @Transactional
    public Optional<Notice> updateNotice(Long id, String title, String content, NoticeVisibility visibility, List<String> imagePaths) {
        return noticeRepository.findById(id).map(notice -> {
            notice.update(title, content, visibility);

            // 기존 이미지 모두 삭제
            noticeImageRepository.deleteAllByNotice(notice);

            // 새 이미지 저장
            if (imagePaths != null && !imagePaths.isEmpty()) {
                for (String path : imagePaths) {
                    NoticeImage image = NoticeImage.of(notice, path);
                    noticeImageRepository.save(image);
                }
            }

            return notice;
        });
    }

    @Transactional
    public boolean deleteNotice(Long id) {
        Optional<Notice> noticeOpt = noticeRepository.findById(id);
        if (noticeOpt.isPresent()) {
            // 이미지 삭제 후 공지사항 삭제
            noticeImageRepository.deleteAllByNotice(noticeOpt.get());
            noticeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
