package ticketing.ticketing.application.dto.concertResponseDto;

import lombok.Builder;
import lombok.Getter;
import ticketing.ticketing.domain.enums.ImagesRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ConcertResponseDto {

    private Long id;
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
    private String concertTag;

    private Long adminId;
    private Long concertHallId;

    private List<ImagesResponseDto> images;
    private ConcertSeatMapResponseDto seatMap;
    private List<ConcertScheduleResponseDto> schedules;
    private List<CastResponseDto> casts;

    @Getter
    @Builder
    public static class ImagesResponseDto {
        private Long id;
        private String imageUrl;  // 파일 URL (절대경로 또는 상대경로)
        private ImagesRole imagesRole;

        public static ImagesResponseDto from(ticketing.ticketing.domain.entity.Images images, String baseImageUrl) {
            return ImagesResponseDto.builder()
                    .id(images.getId())
                    .imageUrl(baseImageUrl + "/" + images.getImage())
                    .imagesRole(images.getImagesRole())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ConcertSeatMapResponseDto {
        private Long id;
        private String originalFileName;
        private String storedFileName;
        private String storedFilePath;

        public static ConcertSeatMapResponseDto from(ticketing.ticketing.domain.entity.ConcertSeatMap seatMap) {
            if (seatMap == null) return null;
            return ConcertSeatMapResponseDto.builder()
                    .id(seatMap.getId())
                    .originalFileName(seatMap.getOriginalFileName())
                    .storedFileName(seatMap.getStoredFileName())
                    .storedFilePath(seatMap.getStoredFilePath())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ConcertScheduleResponseDto {
        private Long id;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public static ConcertScheduleResponseDto from(ticketing.ticketing.domain.entity.ConcertSchedule schedule) {
            return ConcertScheduleResponseDto.builder()
                    .id(schedule.getId())
                    .startTime(schedule.getStartTime())
                    .endTime(schedule.getEndTime())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CastResponseDto {
        private Long id;
        private String name;

        public static CastResponseDto from(ticketing.ticketing.domain.entity.Cast cast) {
            return CastResponseDto.builder()
                    .id(cast.getId())
                    .name(cast.getName())
                    .build();
        }
    }

    // from 메서드 (entity -> dto 변환)
    public static ConcertResponseDto from(ticketing.ticketing.domain.entity.Concert concert, String baseImageUrl) {
        return ConcertResponseDto.builder()
                .id(concert.getId())
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
                .rating(concert.getRating())
                .limitAge(concert.getLimitAge())
                .durationTime(concert.getDurationTime())
                .concertTag(concert.getConcertTag())
                .adminId(concert.getAdmin() != null ? concert.getAdmin().getId() : null)
                .concertHallId(concert.getConcertHall() != null ? concert.getConcertHall().getId() : null)
                .images(concert.getImages().stream()
                        .map(img -> ImagesResponseDto.from(img, baseImageUrl))
                        .collect(Collectors.toList()))
                .seatMap(ConcertSeatMapResponseDto.from(concert.getConcertSeatMap()))
                .schedules(concert.getConcertSchedules().stream()
                        .map(ConcertScheduleResponseDto::from)
                        .collect(Collectors.toList()))
                .casts(concert.getCasts().stream()
                        .map(CastResponseDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
