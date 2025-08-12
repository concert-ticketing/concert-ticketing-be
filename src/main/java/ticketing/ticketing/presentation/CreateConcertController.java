package ticketing.ticketing.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.application.dto.concertCreateRequestDto.ConcertCreateRequestDto;
import ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto;
import ticketing.ticketing.application.dto.concertScheduleRequest.ConcertScheduleRequest;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Concert;
import ticketing.ticketing.domain.enums.ImagesRole;
import ticketing.ticketing.application.service.createConcertService.CreateConcertService;
import ticketing.ticketing.infrastructure.repository.admin.AdminRepository;
import ticketing.ticketing.infrastructure.security.UserContext;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class CreateConcertController {

    private final CreateConcertService createConcertService;
    private final AdminRepository adminRepository;
    private final UserContext userContext;
    private final ObjectMapper objectMapper;

    private final String baseThumbnailUrl = "/upload/thumbnail";
    private final String baseDescriptionUrl = "/upload/description";
    private final String baseSvgImageUrl = "/upload/svg_image";

    // 전체 콘서트 목록 조회
    @GetMapping
    public ResponseEntity<List<ConcertResponseDto>> getAllConcerts() {
        List<ConcertResponseDto> concerts = createConcertService.getAllConcerts(baseThumbnailUrl, baseDescriptionUrl);
        return ResponseEntity.ok(concerts);
    }

    // 단일 콘서트 조회
    @GetMapping("/{id}")
    public ResponseEntity<ConcertResponseDto> getConcert(@PathVariable Long id) {
        Optional<ConcertResponseDto> concertOpt = createConcertService.getConcertById(id, baseThumbnailUrl, baseDescriptionUrl);
        return concertOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 콘서트 생성 (공연회차 리스트 포함)
    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConcertCreateRequestDto> createConcert(
            @RequestPart("concertRequest") CreateConcertRequest request, // 이미 DTO 변환됨
            @RequestPart(value = "scheduleRequests", required = false) List<ConcertScheduleRequest> scheduleRequests,
            @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
            @RequestPart(value = "descriptionImage", required = false) MultipartFile descriptionImage
    ) throws Exception {

        Long adminId = userContext.getCurrentUserId();
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("인증된 관리자를 찾을 수 없습니다."));

        // 서비스 호출
        Concert concert = createConcertService.createConcertWithImagesAndSchedules(
                request.title(),
                request.description(),
                request.location(),
                request.locationX(),
                request.locationY(),
                request.startDate(),
                request.endDate(),
                request.reservationStartDate(),
                request.reservationEndDate(),
                request.price(),
                request.limitAge(),
                request.durationTime(),
                admin,
                request.concertHallName(),
                scheduleRequests,
                thumbnailImage, ImagesRole.THUMBNAIL,
                descriptionImage, ImagesRole.DESCRIPT_IMAGE
        );

        ConcertCreateRequestDto responseDto = ConcertCreateRequestDto.from(concert, baseThumbnailUrl, baseDescriptionUrl);
        return ResponseEntity.created(URI.create("/api/concerts/" + concert.getId())).body(responseDto);
    }

    // 콘서트 수정 (썸네일 + 상세 이미지 + SVG 이미지 + 공연회차)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ConcertResponseDto> updateConcert(
            @PathVariable Long id,
            @RequestPart("concertRequest") String concertRequestJson,
            @RequestPart(value = "scheduleRequests", required = false) String scheduleRequestsJson,
            @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
            @RequestPart(value = "descriptionImage", required = false) MultipartFile descriptionImage,
            @RequestPart(value = "svgImage", required = false) MultipartFile svgImage
    ) throws Exception {
        Long adminId = userContext.getCurrentUserId();
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("인증된 관리자를 찾을 수 없습니다."));

        CreateConcertRequest request = objectMapper.readValue(concertRequestJson, CreateConcertRequest.class);

        List<ConcertScheduleRequest> scheduleRequests = null;
        if (scheduleRequestsJson != null && !scheduleRequestsJson.isEmpty()) {
            scheduleRequests = objectMapper.readValue(scheduleRequestsJson, new TypeReference<List<ConcertScheduleRequest>>() {});
        }

        Optional<Concert> updatedConcertOpt = createConcertService.updateConcertWithImagesAndSchedules(
                id,
                request.title(),
                request.description(),
                request.location(),
                request.locationX(),
                request.locationY(),
                request.startDate(),
                request.endDate(),
                request.reservationStartDate(),
                request.reservationEndDate(),
                request.price(),
                request.rating(),
                request.limitAge(),
                request.durationTime(),
                admin,
                request.concertHallName(),
                scheduleRequests,
                thumbnailImage, ImagesRole.THUMBNAIL,
                descriptionImage, ImagesRole.DESCRIPT_IMAGE,
                svgImage, ImagesRole.SVG_IMAGE
        );

        return updatedConcertOpt
                .map(concert -> ResponseEntity.ok(ConcertResponseDto.from(concert, baseThumbnailUrl, baseDescriptionUrl)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 콘서트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcert(@PathVariable Long id) {
        boolean deleted = createConcertService.deleteConcert(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    public record CreateConcertRequest(
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
            String concertHallName
    ) {}
}
