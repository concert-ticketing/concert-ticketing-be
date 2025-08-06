package ticketing.ticketing.application.dto.noticeResponseDto;

import lombok.Builder;
import lombok.Getter;
import ticketing.ticketing.domain.entity.Notice;
import ticketing.ticketing.domain.entity.NoticeImage;
import ticketing.ticketing.domain.enums.NoticeVisibility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private NoticeVisibility visibility;
    private LocalDateTime createdAt;
    private List<String> imagePaths;

    public static NoticeResponse from(Notice notice) {
        List<String> imagePaths = notice.getImages().stream()
                .map(NoticeImage::getImagePath)
                .collect(Collectors.toList());

        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .visibility(notice.getVisibility())
                .createdAt(notice.getCreatedAt())
                .imagePaths(imagePaths)
                .build();
    }
}
