package ticketing.ticketing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Notice;
import ticketing.ticketing.Repository.NoticeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public Notice createNotice(String title, String content, Admin admin) {
        Notice notice = Notice.create(title, content, admin); // builder 대신 create 메서드 사용
        return noticeRepository.save(notice);
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Optional<Notice> getNotice(Long id) {
        return noticeRepository.findById(id);
    }

    @Transactional
    public Optional<Notice> updateNotice(Long id, String title, String content) {
        return noticeRepository.findById(id).map(notice -> {
            notice.update(title, content);
            return notice;
        });
    }

    @Transactional
    public boolean deleteNotice(Long id) {
        if (noticeRepository.existsById(id)) {
            noticeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
