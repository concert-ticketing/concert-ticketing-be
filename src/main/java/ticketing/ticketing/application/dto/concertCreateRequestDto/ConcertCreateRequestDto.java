package ticketing.ticketing.application.dto.concertCreateRequestDto;

import lombok.Builder;
import lombok.Getter;
import ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto;
import ticketing.ticketing.domain.enums.ImagesRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        public static ConcertCreateRequestDto.ImagesRequestDto from(ticketing.ticketing.domain.entity.Images images, String baseThumbnailUrl, String baseDescriptionUrl) {
            String baseUrl = images.getImagesRole() == ImagesRole.THUMBNAIL ? baseThumbnailUrl : baseDescriptionUrl;
            return ConcertCreateRequestDto.ImagesRequestDto.builder()
                    .image(baseUrl + "/" + images.getImage())
                    .imagesRole(images.getImagesRole())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ConcertSeatMapRequestDto {
        private String originalFileName;
        private String storedFileName;
        private String storedFilePath;
        public static ConcertCreateRequestDto.ConcertSeatMapRequestDto from(ticketing.ticketing.domain.entity.ConcertSeatMap seatMap) {
            if (seatMap == null) return null;
            return ConcertCreateRequestDto.ConcertSeatMapRequestDto.builder()
                    .originalFileName(seatMap.getOriginalFileName())
                    .storedFileName(seatMap.getStoredFileName())
                    .storedFilePath(seatMap.getStoredFilePath())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ConcertScheduleRequestDto {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        public static ConcertCreateRequestDto.ConcertScheduleRequestDto from(ticketing.ticketing.domain.entity.ConcertSchedule schedule) {
            return ConcertCreateRequestDto.ConcertScheduleRequestDto.builder()
                    .startTime(schedule.getStartTime())
                    .endTime(schedule.getEndTime())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CastRequestDto {
        private String name;
        private Long adminId;
        public static ConcertCreateRequestDto.CastRequestDto from(ticketing.ticketing.domain.entity.Cast cast) {
            return ConcertCreateRequestDto.CastRequestDto.builder()
                    .name(cast.getName())
                    .build();
        }
    }

    // 기존 from (baseImageUrl 1개 인자)
    public static ConcertCreateRequestDto from(ticketing.ticketing.domain.entity.Concert concert, String baseImageUrl) {
        return ConcertCreateRequestDto.builder()
                .title(concert.getTitle())
                .description(concert.getDescription())
                .location(concert.getLocation())
                .locationX(concert.getLocationX())
                .locationY(concert.getLocationY())
                .startDate(concert.getStartDate())
                .endDate(concert.getEndDate())
                .reservationStartDate(concert.getReservationStartDate())
                .reservationEndDate(concert.getReservationEndDate())
                .price(concert.getPrice())
                .limitAge(concert.getLimitAge())
                .durationTime(concert.getDurationTime())
                .adminId(concert.getAdmin() != null ? concert.getAdmin().getId() : null)
                .concertHallId(concert.getConcertHall() != null ? concert.getConcertHall().getId() : null)
                .images(concert.getImages().stream()
                        .map(img -> ConcertCreateRequestDto.ImagesRequestDto.from(img, baseImageUrl, baseImageUrl))
                        .collect(Collectors.toList()))
                .seatMap(ConcertCreateRequestDto.ConcertSeatMapRequestDto.from(concert.getConcertSeatMap()))
                .schedules(concert.getConcertSchedules().stream()
                        .map(ConcertCreateRequestDto.ConcertScheduleRequestDto::from)
                        .collect(Collectors.toList()))
                .casts(concert.getCasts().stream()
                        .map(ConcertCreateRequestDto.CastRequestDto::from)
                        .collect(Collectors.toList()))
                .build();
    }


    public static ConcertCreateRequestDto from(ticketing.ticketing.domain.entity.Concert concert, String baseThumbnailUrl, String baseDescriptionUrl) {
        return ConcertCreateRequestDto.builder()
                .title(concert.getTitle())
                .description(concert.getDescription())
                .location(concert.getLocation())
                .locationX(concert.getLocationX())
                .locationY(concert.getLocationY())
                .startDate(concert.getStartDate())
                .endDate(concert.getEndDate())
                .reservationStartDate(concert.getReservationStartDate())
                .reservationEndDate(concert.getReservationEndDate())
                .price(concert.getPrice())
                .limitAge(concert.getLimitAge())
                .durationTime(concert.getDurationTime())
                .adminId(concert.getAdmin() != null ? concert.getAdmin().getId() : null)
                .concertHallId(concert.getConcertHall() != null ? concert.getConcertHall().getId() : null)
                .images(concert.getImages().stream()
                        .map(img -> ConcertCreateRequestDto.ImagesRequestDto.from(img, baseThumbnailUrl, baseDescriptionUrl))
                        .collect(Collectors.toList()))
                .seatMap(ConcertCreateRequestDto.ConcertSeatMapRequestDto.from(concert.getConcertSeatMap()))
                .schedules(concert.getConcertSchedules().stream()
                        .map(ConcertCreateRequestDto.ConcertScheduleRequestDto::from)
                        .collect(Collectors.toList()))
                .casts(concert.getCasts().stream()
                        .map(ConcertCreateRequestDto.CastRequestDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
