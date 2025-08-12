package ticketing.ticketing.application.service.createConcertService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.application.dto.concertScheduleRequest.ConcertScheduleRequest;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Concert;
import ticketing.ticketing.domain.entity.ConcertSchedule;
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
    public List<ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto> getAllConcerts(String baseThumbnailUrl, String baseDescriptionUrl) {
        return createConcertRepository.findAll()
                .stream()
                .map(c -> ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto.from(c, baseThumbnailUrl, baseDescriptionUrl))
                .collect(Collectors.toList());
    }

    // 단일 콘서트 조회
    public Optional<ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto> getConcertById(Long id, String baseThumbnailUrl, String baseDescriptionUrl) {
        return createConcertRepository.findById(id)
                .map(c -> ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto.from(c, baseThumbnailUrl, baseDescriptionUrl));
    }

    // 콘서트 등록 (공연회차 리스트 포함)
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

        // 공연회차 생성 및 연관관계 설정 (수정된 순서: Concert, startTime, endTime)
        if (scheduleRequests != null) {
            for (ConcertScheduleRequest scheduleRequest : scheduleRequests) {
                ConcertSchedule schedule = ConcertSchedule.create(concert, scheduleRequest.getStartTime(), scheduleRequest.getEndTime());
                concert.getConcertSchedules().add(schedule);
            }
        }

        return createConcertRepository.save(concert);
    }

    // 콘서트 수정 (SVG 이미지 처리 포함) - 공연회차 수정도 포함 가능
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
            MultipartFile thumbnailImage,
            ImagesRole thumbnailRole,
            MultipartFile descriptionImage,
            ImagesRole descriptionRole,
            MultipartFile svgImage,
            ImagesRole svgRole
    ) throws Exception {

        return createConcertRepository.findById(id).map(concert -> {
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
                if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
                    String thumbnailFileName = saveImage(thumbnailImage, thumbnailPath);
                    concert.addImage(thumbnailFileName, thumbnailRole);
                }
                if (descriptionImage != null && !descriptionImage.isEmpty()) {
                    String descriptionFileName = saveImage(descriptionImage, descriptionPath);
                    concert.addImage(descriptionFileName, descriptionRole);
                }
                if (svgImage != null && !svgImage.isEmpty()) {
                    String svgFileName = saveImage(svgImage, svgImagePath);
                    concert.addImage(svgFileName, svgRole);
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }

            // 공연회차 업데이트 로직 (기존 회차 모두 삭제 후 재등록 예시)
            if (scheduleRequests != null) {
                concert.getConcertSchedules().clear(); // 기존 일정 제거
                for (ConcertScheduleRequest scheduleRequest : scheduleRequests) {
                    ConcertSchedule schedule = ConcertSchedule.create(concert, scheduleRequest.getStartTime(), scheduleRequest.getEndTime());
                    concert.getConcertSchedules().add(schedule);
                }
            }

            return concert;
        });
    }

    // 논리 삭제
    @Transactional
    public boolean deleteConcert(Long id) {
        return createConcertRepository.findById(id).map(concert -> {
            concert.deleteLogical();
            createConcertRepository.save(concert);
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
