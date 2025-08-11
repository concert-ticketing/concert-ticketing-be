package ticketing.ticketing.application.dto.concertCreateRequestDto;

import lombok.Builder;
import lombok.Getter;
import ticketing.ticketing.domain.enums.ImagesRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ConcertCreateRequestDto {

    private String title;
    private String description;
    private String location;
    private BigDecimal locationX;
    private BigDecimal locationY;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime reservationStartDate;
    private LocalDateTime reservationEndDate;
    private String price;
    private int rating;
    private int limitAge;
    private int durationTime;

    private Long adminId;

    private Long concertHallId;

    // 이미지는 이미지 DTO 리스트로 처리
    private List<ImagesRequestDto> images;

    // 콘서트 좌석맵 (파일명이나 경로 등)
    private ConcertSeatMapRequestDto seatMap;

    // 스케줄 리스트
    private List<ConcertScheduleRequestDto> schedules;

    // 캐스트 리스트
    private List<CastRequestDto> casts;

    @Getter
    @Builder
    public static class ImagesRequestDto {
        private String image;
        private ImagesRole imagesRole;
    }

    @Getter
    @Builder
    public static class ConcertSeatMapRequestDto {
        private String originalFileName;
        private String storedFileName;
        private String storedFilePath;
    }

    @Getter
    @Builder
    public static class ConcertScheduleRequestDto {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

    @Getter
    @Builder
    public static class CastRequestDto {
        private String name;
        private Long adminId;
    }
}
