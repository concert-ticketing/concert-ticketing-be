package ticketing.ticketing.application.service.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.ticketing.application.dto.noticeCreateRequest.NoticeCreateRequest;
import ticketing.ticketing.application.dto.noticeUpdateRequest.NoticeUpdateRequest;
import ticketing.ticketing.application.dto.noticeResponseDto.NoticeResponse;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Notice;
import ticketing.ticketing.domain.entity.NoticeImage;
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

    @Value("${upload.path.notice}")
    private String uploadPath;

    @Transactional
    public NoticeResponse createNotice(NoticeCreateRequest request, Admin admin) {
        Notice notice = Notice.create(
                request.getTitle(),
                request.getContent(),
                admin,
                request.getVisibility()
        );
        Notice savedNotice = noticeRepository.save(notice);

        if (request.getImagePaths() != null && !request.getImagePaths().isEmpty()) {
            for (String path : request.getImagePaths()) {
                NoticeImage image = NoticeImage.of(savedNotice, path);
                noticeImageRepository.save(image);
            }
        }

        return NoticeResponse.from(savedNotice);
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
    public Optional<NoticeResponse> updateNotice(Long id, NoticeUpdateRequest request) {
        return noticeRepository.findById(id).map(notice -> {
            notice.update(request.getTitle(), request.getContent(), request.getVisibility());

            // 기존 이미지 삭제
            noticeImageRepository.deleteAllByNotice(notice);

            // 새 이미지 저장
            if (request.getImagePaths() != null && !request.getImagePaths().isEmpty()) {
                for (String path : request.getImagePaths()) {
                    NoticeImage image = NoticeImage.of(notice, path);
                    noticeImageRepository.save(image);
                }
            }

            return NoticeResponse.from(notice);
        });
    }

    @Transactional
    public boolean deleteNotice(Long id) {
        Optional<Notice> noticeOpt = noticeRepository.findById(id);
        if (noticeOpt.isPresent()) {
            noticeImageRepository.deleteAllByNotice(noticeOpt.get());
            noticeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
