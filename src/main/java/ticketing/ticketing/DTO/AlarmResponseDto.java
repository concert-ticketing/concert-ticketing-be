package ticketing.ticketing.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ticketing.ticketing.domain.entity.Alarm;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AlarmResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;

    public static AlarmResponseDto from(Alarm alarm) {
        return AlarmResponseDto.builder()
                .id(alarm.getId())
                .title(alarm.getTitle())
                .description(alarm.getDescription())
                .createdAt(alarm.getCreatedAt())
                .build();
    }
}
