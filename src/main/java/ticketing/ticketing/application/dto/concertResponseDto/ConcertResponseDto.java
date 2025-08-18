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

    private Long adminId;
    private String concertHallName;

    private List<ImagesResponseDto> images;
    private ConcertSeatMapResponseDto seatMap;
    private List<ConcertScheduleResponseDto> schedules;
    private List<CastResponseDto> casts;

    // 좌석 구역 리스트 추가
    private List<ConcertSeatSectionResponseDto> seatSections;

    @Getter
    @Builder
    public static class ImagesResponseDto {
        private Long id;
        private String image;
        private ImagesRole imagesRole;

        public static ImagesResponseDto from(ticketing.ticketing.domain.entity.Images images) {
            return ImagesResponseDto.builder()
                    .id(images.getId())
                    .image(resolveImagePath(images))
                    .imagesRole(images.getImagesRole())
                    .build();
        }

        private static String resolveImagePath(ticketing.ticketing.domain.entity.Images images) {
            switch (images.getImagesRole()) {
                case THUMBNAIL:
                    return "/uploads/thumbnail/" + images.getImage();
                case DESCRIPT_IMAGE:
                    return "/uploads/description/" + images.getImage();
                case SVG_IMAGE:
                    return "/uploads/svg_image/" + images.getImage();
            }
            throw new IllegalArgumentException("지원하지 않는 이미지 타입: " + images.getImagesRole());
        }
    }

    @Getter
    @Builder
    public static class ConcertSeatMapResponseDto {
        private Long id;
        private String originalFileName;
        private String storedFileName;

        public static ConcertSeatMapResponseDto from(ticketing.ticketing.domain.entity.ConcertSeatMap seatMap) {
            if (seatMap == null) return null;
            return ConcertSeatMapResponseDto.builder()
                    .id(seatMap.getId())
                    .originalFileName(seatMap.getOriginalFileName())
                    .storedFileName(seatMap.getStoredFileName() != null
                            ? "/uploads/seatmap/" + seatMap.getStoredFileName()
                            : null)
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

    // 좌석 구역 DTO 추가
    @Getter
    @Builder
    public static class ConcertSeatSectionResponseDto {
        private Long id;
        private String sectionName;
        private String colorCode;
        private BigDecimal price;
        private List<ConcertSeatResponseDto> seats;

        public static ConcertSeatSectionResponseDto from(ticketing.ticketing.domain.entity.ConcertSeatSection section) {
            return ConcertSeatSectionResponseDto.builder()
                    .id(section.getId())
                    .sectionName(section.getSectionName())
                    .colorCode(section.getColorCode())
                    .price(section.getPrice())
                    .seats(section.getSeats().stream()
                            .map(ConcertSeatResponseDto::from)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ConcertSeatResponseDto {
        private Long id;
        private String rowName;
        private String seatNumber;
        private String price;

        public static ConcertSeatResponseDto from(ticketing.ticketing.domain.entity.ConcertSeat seat) {
            return ConcertSeatResponseDto.builder()
                    .id(seat.getId())
                    .rowName(seat.getRowName())
                    .seatNumber(seat.getSeatNumber() != null ? seat.getSeatNumber().toString() : null)
                    .price(seat.getPrice() != null ? seat.getPrice().toString() : null)
                    .build();
        }
    }

    public static ConcertResponseDto from(ticketing.ticketing.domain.entity.Concert concert) {
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
                .adminId(concert.getAdmin() != null ? concert.getAdmin().getId() : null)
                .concertHallName(concert.getConcertHallName())
                .images(concert.getImages().stream()
                        .map(ImagesResponseDto::from)
                        .collect(Collectors.toList()))
                .seatMap(ConcertSeatMapResponseDto.from(concert.getConcertSeatMap()))
                .schedules(concert.getConcertSchedules().stream()
                        .map(ConcertScheduleResponseDto::from)
                        .collect(Collectors.toList()))
                .casts(concert.getCasts().stream()
                        .map(CastResponseDto::from)
                        .collect(Collectors.toList()))
                .seatSections(concert.getConcertSeatSections().stream()
                        .map(ConcertSeatSectionResponseDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
