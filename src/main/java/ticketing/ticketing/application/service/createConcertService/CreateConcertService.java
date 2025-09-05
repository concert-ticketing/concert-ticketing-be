package ticketing.ticketing.application.service.createConcertService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.application.dto.concertScheduleRequest.ConcertScheduleRequest;
import ticketing.ticketing.application.dto.concertSeatRequestDto.ConcertSeatRequestDto;
import ticketing.ticketing.application.dto.concertSeatRequestDto.ConcertSeatSectionRequestDto;
import ticketing.ticketing.domain.entity.*;
import ticketing.ticketing.domain.enums.ImagesRole;
import ticketing.ticketing.infrastructure.repository.createConcert.CreateConcertRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
        return createConcertRepository.findAll().stream()
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

        Concert concert = Concert.create(title, description, location, locationX, locationY,
                startDate, endDate, reservationStartDate, reservationEndDate,
                price, limitAge, durationTime, admin, concertHallName);

        // 이미지 저장
        saveAndAddImage(concert, thumbnailImage, thumbnailRole, thumbnailPath);
        saveAndAddImage(concert, descriptionImage, descriptionRole, descriptionPath);

        // 공연회차 생성
        if (scheduleRequests != null) {
            for (ConcertScheduleRequest req : scheduleRequests) {
                ConcertSchedule schedule = ConcertSchedule.create(concert, req.getStartTime());
                concert.getConcertSchedules().add(schedule);
            }
        }

        return createConcertRepository.save(concert);
    }

    // 콘서트 수정
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
            Integer rating,
            Integer limitAge,
            Integer durationTime,
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
                    rating != null ? rating : concert.getRating(),
                    limitAge != null ? limitAge : concert.getLimitAge(),
                    durationTime != null ? durationTime : concert.getDurationTime(),
                    admin != null ? admin : concert.getAdmin(),
                    concertHallName != null ? concertHallName : concert.getConcertHallName()
            );

            try {
                // 이미지 업데이트
                saveAndReplaceImage(concert, thumbnailImage, thumbnailRole, thumbnailPath);
                saveAndReplaceImage(concert, descriptionImage, descriptionRole, descriptionPath);
                saveAndReplaceImage(concert, svgImage, svgRole, svgImagePath);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }

            // 공연회차 생성
            List<ConcertSchedule> updatedSchedules = new ArrayList<>();
            if (scheduleRequests != null) {
                for (ConcertScheduleRequest req : scheduleRequests) {
                    ConcertSchedule schedule = ConcertSchedule.create(concert, req.getStartTime());
                    concert.getConcertSchedules().add(schedule);
                    updatedSchedules.add(schedule);
                }
            }

            // 좌석 및 구역 업데이트
            if (seatSections != null) {
                for (ConcertSeatSectionRequestDto sectionDto : seatSections) {
                    ConcertSeatSection section = concert.getConcertSeatSections().stream()
                            .filter(sec -> sec.getColorCode().equals(sectionDto.getColorCode()))
                            .findFirst()
                            .orElseGet(() -> {
                                ConcertSeatSection newSection = ConcertSeatSection.create(
                                        sectionDto.getSectionName(),
                                        sectionDto.getColorCode(),
                                        sectionDto.getPrice(),
                                        concert
                                );
                                concert.getConcertSeatSections().add(newSection);
                                return newSection;
                            });

                    if (sectionDto.getSectionName() != null) section.updateSectionName(sectionDto.getSectionName());
                    if (sectionDto.getPrice() != null) section.updatePrice(sectionDto.getPrice());

                    if (sectionDto.getSeats() != null) {
                        for (ConcertSeatRequestDto seatDto : sectionDto.getSeats()) {
                            boolean exists = section.getSeats().stream()
                                    .anyMatch(s -> s.getRowName().equals(seatDto.getRowName())
                                            && s.getSeatNumber().equals(seatDto.getSeatNumber()));
                            if (!exists) {
                                ConcertSeat seat = ConcertSeat.create(seatDto.getRowName(), seatDto.getSeatNumber(), section);
                                // 모든 공연회차에 연결
                                for (ConcertSchedule schedule : updatedSchedules) {
                                    schedule.addConcertSeat(seat);
                                }
                                section.getSeats().add(seat);
                            }
                        }
                    }
                }
            }

            return createConcertRepository.save(concert);
        });
    }

    @Transactional
    public boolean deleteConcert(Long id) {
        return createConcertRepository.findById(id)
                .map(concert -> {
                    createConcertRepository.delete(concert);
                    return true;
                }).orElse(false);
    }

    // 이미지 저장 및 추가
    private void saveAndAddImage(Concert concert, MultipartFile file, ImagesRole role, String path) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = saveImage(file, path);
            concert.addImage(fileName, role);
        }
    }

    // 이미지 저장 및 기존 이미지 교체
    private void saveAndReplaceImage(Concert concert, MultipartFile file, ImagesRole role, String path) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = saveImage(file, path);
            concert.getImages().removeIf(img -> img.getImagesRole() == role);
            concert.addImage(fileName, role);
        }
    }

    // 파일 저장
    private String saveImage(MultipartFile file, String uploadDir) throws IOException {
        String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(file.getOriginalFilename());
        File dest = new File(uploadDir, fileName);
        if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();
        file.transferTo(dest);
        return fileName;
    }
}