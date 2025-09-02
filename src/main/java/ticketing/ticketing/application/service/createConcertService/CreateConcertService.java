package ticketing.ticketing.application.service.createConcertService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.application.dto.ConcertSeatRequestDto;
import ticketing.ticketing.application.dto.concertScheduleRequest.ConcertScheduleRequest;
import ticketing.ticketing.application.dto.concertSeatRequestDto.ConcertSeatSectionRequestDto;
import ticketing.ticketing.domain.entity.*;
import ticketing.ticketing.domain.enums.ImagesRole;
import ticketing.ticketing.infrastructure.repository.createConcert.CreateConcertRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateConcertService {

    private final CreateConcertRepository createConcertRepository;

    @Value("${upload.path.thumbnail}")
    private String thumbnailPath;

    @Value("${upload.path.description}")
    private String descriptionPath;

    @Value("${upload.path.svg_image}")
    private String svgImagePath;

    // 전체 콘서트 조회
    public List<ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto> getAllConcerts() {
        return createConcertRepository.findAll()
                .stream()
                .map(ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto::from)
                .collect(Collectors.toList());
    }

    // 단일 콘서트 조회
    public Optional<ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto> getConcertById(Long id) {
        return createConcertRepository.findById(id)
                .map(ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto::from);
    }

    // 콘서트 등록
    @Transactional
    public Concert createConcertWithImagesAndSchedules(
            String title,
            String description,
            String location,
            BigDecimal locationX,
            BigDecimal locationY,
            LocalDate startDate,
            LocalDate endDate,
            LocalDateTime reservationStartDate,
            LocalDateTime reservationEndDate,
            String price,
            int limitAge,
            int durationTime,
            Admin admin,
            String concertHallName,
            List<ConcertScheduleRequest> scheduleRequests,
            MultipartFile thumbnailImage,
            ImagesRole thumbnailRole,
            MultipartFile descriptionImage,
            ImagesRole descriptionRole
    ) throws Exception {

        String thumbnailFileName = saveImage(thumbnailImage, thumbnailPath);
        String descriptionFileName = saveImage(descriptionImage, descriptionPath);

        Concert concert = Concert.create(
                title,
                description,
                location,
                locationX,
                locationY,
                startDate,
                endDate,
                reservationStartDate,
                reservationEndDate,
                price,
                limitAge,
                durationTime,
                admin,
                concertHallName
        );

        if (thumbnailFileName != null) {
            concert.addImage(thumbnailFileName, thumbnailRole);
        }
        if (descriptionFileName != null) {
            concert.addImage(descriptionFileName, descriptionRole);
        }

        if (scheduleRequests != null) {
            for (ConcertScheduleRequest scheduleRequest : scheduleRequests) {
                ConcertSchedule schedule = ConcertSchedule.create(concert, scheduleRequest.getConcertTime());
                concert.getConcertSchedules().add(schedule);
            }
        }

        return createConcertRepository.save(concert);
    }

    // 콘서트 수정 (이미지 덮어쓰기, 공연회차/좌석 포함)
    @Transactional
    public Optional<Concert> updateConcertWithImagesAndSchedules(
            Long id,
            String title,
            String description,
            String location,
            BigDecimal locationX,
            BigDecimal locationY,
            LocalDate startDate,
            LocalDate endDate,
            LocalDateTime reservationStartDate,
            LocalDateTime reservationEndDate,
            String price,
            int rating,
            int limitAge,
            int durationTime,
            Admin admin,
            String concertHallName,
            List<ConcertScheduleRequest> scheduleRequests,
            List<ConcertSeatSectionRequestDto> seatSections,
            MultipartFile thumbnailImage,
            ImagesRole thumbnailRole,
            MultipartFile descriptionImage,
            ImagesRole descriptionRole,
            MultipartFile svgImage,
            ImagesRole svgRole
    ) throws Exception {

        return createConcertRepository.findById(id).map(concert -> {
            // 콘서트 기본 정보 업데이트 (null 아닐 때만 덮어쓰기)
            concert.update(
                    title != null ? title : concert.getTitle(),
                    description != null ? description : concert.getDescription(),
                    location != null ? location : concert.getLocation(),
                    locationX != null ? locationX : concert.getLocationX(),
                    locationY != null ? locationY : concert.getLocationY(),
                    startDate != null ? startDate : concert.getStartDate(),
                    endDate != null ? endDate : concert.getEndDate(),
                    reservationStartDate != null ? reservationStartDate : concert.getReservationStartDate(),
                    reservationEndDate != null ? reservationEndDate : concert.getReservationEndDate(),
                    price != null ? price : concert.getPrice(),
                    rating != 0 ? rating : concert.getRating(), // int는 null 체크 대신 기본값으로 구분
                    limitAge != 0 ? limitAge : concert.getLimitAge(),
                    durationTime != 0 ? durationTime : concert.getDurationTime(),
                    admin != null ? admin : concert.getAdmin(),
                    concertHallName != null ? concertHallName : concert.getConcertHallName()
            );

            try {
                // 이미지 업데이트: null → 유지, 값 있으면 교체
                if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
                    String thumbnailFileName = saveImage(thumbnailImage, thumbnailPath);
                    concert.getImages().removeIf(img -> img.getImagesRole() == ImagesRole.THUMBNAIL);
                    concert.addImage(thumbnailFileName, thumbnailRole);
                }
                if (descriptionImage != null && !descriptionImage.isEmpty()) {
                    String descriptionFileName = saveImage(descriptionImage, descriptionPath);
                    concert.getImages().removeIf(img -> img.getImagesRole() == ImagesRole.DESCRIPT_IMAGE);
                    concert.addImage(descriptionFileName, descriptionRole);
                }
                if (svgImage != null && !svgImage.isEmpty()) {
                    String svgFileName = saveImage(svgImage, svgImagePath);
                    concert.getImages().removeIf(img -> img.getImagesRole() == ImagesRole.SVG_IMAGE);
                    concert.addImage(svgFileName, svgRole);
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }

            // 공연회차 업데이트 (null → 유지, 빈 리스트 → 전체 삭제 후 없음으로 유지)
            if (scheduleRequests != null) {
                concert.getConcertSchedules().clear();
                for (ConcertScheduleRequest scheduleRequest : scheduleRequests) {
                    ConcertSchedule schedule = ConcertSchedule.create(concert, scheduleRequest.getConcertTime());
                    concert.getConcertSchedules().add(schedule);
                }
            }

            // 좌석 및 구역 업데이트 (null → 유지, 빈 리스트 → 전체 삭제 후 없음으로 유지)
            if (seatSections != null) {
                concert.getConcertSeatSections().clear();

                for (ConcertSeatSectionRequestDto sectionDto : seatSections) {
                    ConcertSeatSection section = ConcertSeatSection.create(
                            sectionDto.getSectionName(),
                            sectionDto.getColorCode(),
                            sectionDto.getPrice(),
                            concert
                    );

                    if (sectionDto.getSeats() != null) {
                        for (ConcertSeatRequestDto seatDto : sectionDto.getSeats()) {
                            ConcertSeat seat = ConcertSeat.create(
                                    seatDto.getRowName(),
                                    seatDto.getSeatNumber(),
                                    seatDto.getPrice(),
                                    section
                            );
                            section.getSeats().add(seat);
                        }
                    }

                    concert.getConcertSeatSections().add(section);
                }
            }

            return concert;
        });
    }

    @Transactional
    public boolean deleteConcert(Long id) {
        return createConcertRepository.findById(id).map(concert -> {
            createConcertRepository.delete(concert);
            return true;
        }).orElse(false);
    }

    // 이미지 저장 공통 메서드
    private String saveImage(MultipartFile file, String uploadDir) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir, fileName);
            File parent = dest.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            file.transferTo(dest);
            return fileName;
        }
        return null;
    }
}
