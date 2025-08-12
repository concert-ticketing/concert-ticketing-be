/*
package ticketing.ticketing.application.dto.concertHallResponseDto;

import lombok.Getter;
import ticketing.ticketing.application.dto.concertHallAreaRequestDto.ConcertHallAreaRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ConcertHallResponseDto {
    private Long id;
    private String concertHallName;
    private LocalDateTime createdAt;
    private List<ConcertHallAreaRequestDto> areas;

    private ConcertHallResponseDto(Long id, String concertHallName, LocalDateTime createdAt, List<ConcertHallAreaRequestDto> areas) {
        this.id = id;
        this.concertHallName = concertHallName;
        this.createdAt = createdAt;
        this.areas = areas;
    }

    // createdAt 파라미터 추가
    public static ConcertHallResponseDto of(Long id, String concertHallName, LocalDateTime createdAt, List<ConcertHallAreaRequestDto> areas) {
        return new ConcertHallResponseDto(id, concertHallName, createdAt, areas);
    }
}
*/
