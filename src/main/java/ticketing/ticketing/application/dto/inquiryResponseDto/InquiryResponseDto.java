package ticketing.ticketing.application.dto.inquiryResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.entity.Inquiry;
import ticketing.ticketing.domain.entity.InquiryFile;
import ticketing.ticketing.domain.enums.InquiryStatus;
import ticketing.ticketing.domain.enums.InquiryType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InquiryResponseDto {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String title;
    private String content;
    private InquiryType type;
    private InquiryStatus status;
    private String answer;
    private LocalDateTime repliedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private List<String> imagePaths;

    public static InquiryResponseDto fromEntity(Inquiry inquiry) {
        List<String> imagePaths = inquiry.getInquiryFiles() != null
                ? inquiry.getInquiryFiles().stream()
                .map(InquiryFile::getFilePath)
                .collect(Collectors.toList())
                : List.of();

        return new InquiryResponseDto(
                inquiry.getId(),
                inquiry.getUser().getId(),
                inquiry.getUser().getName(),
                inquiry.getUser().getEmail(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getType(),
                inquiry.getStatus(),
                inquiry.getAnswer(),
                inquiry.getRepliedAt(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt(),
                inquiry.getDeletedAt(),
                imagePaths
        );
    }
}
