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

        // 공연 회차 등록 (concertTime만 사용)
        if (scheduleRequests != null) {
            for (ConcertScheduleRequest scheduleRequest : scheduleRequests) {
                ConcertSchedule schedule = ConcertSchedule.create(
                        concert,
                        scheduleRequest.getStartTime()
                );
                concert.getConcertSchedules().add(schedule);
            }
        }

        return createConcertRepository.save(concert);
    }

    // 콘서트 수정 (이미지 덮어쓰기, 공연회차/좌석/구역 포함)
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
            // 콘서트 기본 정보 업데이트
            concert.update(
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
                    rating,
                    limitAge,
                    durationTime,
                    admin,
                    concertHallName
            );

            try {
                // 이미지 덮어쓰기
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

            // 공연회차 업데이트 (concertTime만 사용)
            if (scheduleRequests != null) {
                concert.getConcertSchedules().clear();
                for (ConcertScheduleRequest scheduleRequest : scheduleRequests) {
                    ConcertSchedule schedule = ConcertSchedule.create(
                            concert,
                            scheduleRequest.getStartTime()
                    );
                    concert.getConcertSchedules().add(schedule);
                }
            }

            // 좌석 및 구역 업데이트 (colorCode로 섹션 식별, 구역 가격/이름 갱신)
            if (seatSections != null) {
                for (ConcertSeatSectionRequestDto sectionDto : seatSections) {
                    // 1) colorCode 기준으로 기존 섹션 조회
                    Optional<ConcertSeatSection> existingSectionOpt = concert.getConcertSeatSections().stream()
                            .filter(sec -> sec.getColorCode().equals(sectionDto.getColorCode()))
                            .findFirst();

                    ConcertSeatSection section;
                    if (existingSectionOpt.isPresent()) {
                        section = existingSectionOpt.get();
                        // 섹션명/가격 업데이트 (요청값 반영)
                        if (sectionDto.getSectionName() != null) {
                            section.updateSectionName(sectionDto.getSectionName());
                        }
                        if (sectionDto.getPrice() != null) {
                            section.updatePrice(sectionDto.getPrice());
                        }
                    } else {
                        boolean duplicated = concert.getConcertSeatSections().stream()
                                .anyMatch(sec -> sec.getColorCode().equals(sectionDto.getColorCode()));
                        if (duplicated) {
                            throw new IllegalArgumentException("해당 콘서트에 이미 같은 colorCode가 존재합니다: " + sectionDto.getColorCode());
                        }

                        section = ConcertSeatSection.create(
                                sectionDto.getSectionName(),
                                sectionDto.getColorCode(),
                                sectionDto.getPrice(),
                                concert
                        );
                        concert.getConcertSeatSections().add(section);
                    }

                    // 좌석 추가: Row + SeatNumber 기준 중복 방지 (좌석 가격 없음)
                    if (sectionDto.getSeats() != null) {
                        for (ConcertSeatRequestDto seatDto : sectionDto.getSeats()) {
                            boolean seatExists = section.getSeats().stream()
                                    .anyMatch(s -> s.getRowName().equals(seatDto.getRowName())
                                            && s.getSeatNumber().equals(seatDto.getSeatNumber()));
                            if (!seatExists) {
                                ConcertSeat seat = ConcertSeat.create(
                                        seatDto.getRowName(),
                                        seatDto.getSeatNumber(),
                                        section
                                );
                                section.getSeats().add(seat);
                            }
                        }
                    }
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
