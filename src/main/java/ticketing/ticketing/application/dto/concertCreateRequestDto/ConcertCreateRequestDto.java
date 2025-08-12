package ticketing.ticketing.application.dto.concertCreateRequestDto;

import lombok.Builder;
import lombok.Getter;
import ticketing.ticketing.domain.entity.Concert;
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

    private String concertHallName;

    // 이미지는 이미지 DTO 리스트로 처리 (파일명만)
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
        private String image;       // 파일명만 저장
        private ImagesRole imagesRole;

        public static ImagesRequestDto from(ticketing.ticketing.domain.entity.Images images) {
            return ImagesRequestDto.builder()
                    .image(images.getImage())    // baseUrl 없이 파일명만 반환
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

        public static ConcertSeatMapRequestDto from(ticketing.ticketing.domain.entity.ConcertSeatMap seatMap) {
            if (seatMap == null) return null;
            return ConcertSeatMapRequestDto.builder()
                    .originalFileName(seatMap.getOriginalFileName())
                    .storedFileName(seatMap.getStoredFileName())
                    .storedFilePath(seatMap.getStoredFilePath())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ConcertScheduleRequestDto {
        private LocalDateTime concertTime;

            public static ConcertScheduleRequestDto from(ticketing.ticketing.domain.entity.ConcertSchedule schedule) {
                return ConcertScheduleRequestDto.builder()
                        .concertTime(schedule.getConcertTime())
                        .build();
            }
        }

        @Getter
        @Builder
        public static class CastRequestDto {
            private String name;
            private Long adminId;

            public static CastRequestDto from(ticketing.ticketing.domain.entity.Cast cast) {
                return CastRequestDto.builder()
                        .name(cast.getName())
                        .adminId(cast.getAdmin() != null ? cast.getAdmin().getId() : null)
                        .build();
            }
        }

        // baseUrl 없이 단일 from 메서드로 통일
        public static ConcertCreateRequestDto from(Concert concert) {
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
                    .concertHallName(concert.getConcertHallName())
                    .images(concert.getImages().stream()
                            .map(ImagesRequestDto::from) // baseUrl 없이 파일명만 매핑
                            .collect(Collectors.toList()))
                    .seatMap(ConcertSeatMapRequestDto.from(concert.getConcertSeatMap()))
                    .schedules(concert.getConcertSchedules().stream()
                            .map(ConcertScheduleRequestDto::from)
                            .collect(Collectors.toList()))
                    .casts(concert.getCasts().stream()
                            .map(CastRequestDto::from)
                            .collect(Collectors.toList()))
                    .build();
        }
    }
