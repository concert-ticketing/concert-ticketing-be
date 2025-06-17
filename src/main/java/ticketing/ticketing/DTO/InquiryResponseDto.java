package ticketing.ticketing.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.entity.Inquiry;

import java.time.LocalDateTime;

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
    private String type;
    private String status;
    private LocalDateTime repliedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static InquiryResponseDto fromEntity(Inquiry inquiry) {
        return new InquiryResponseDto(
                inquiry.getId(),
                inquiry.getUser().getId(),
                inquiry.getUser().getName(),
                inquiry.getUser().getEmail(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getType().name(),
                inquiry.getStatus().name(),
                inquiry.getRepliedAt(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt(),
                inquiry.getDeletedAt()
        );
    }
}