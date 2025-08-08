package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Concert;
import ticketing.ticketing.application.service.createConcertService.CreateConcertService;
import ticketing.ticketing.infrastructure.repository.admin.AdminRepository;
import ticketing.ticketing.infrastructure.security.UserContext;

import jakarta.validation.Valid;
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

    private final String baseImageUrl = "/upload/image";

    // 전체 콘서트 목록 조회
    @GetMapping
    public ResponseEntity<List<ConcertResponseDto>> getAllConcerts() {
        List<ConcertResponseDto> concerts = createConcertService.getAllConcerts(baseImageUrl);
        return ResponseEntity.ok(concerts);
    }

    // 단일 콘서트 조회
    @GetMapping("/{id}")
    public ResponseEntity<ConcertResponseDto> getConcert(@PathVariable Long id) {
        Optional<ConcertResponseDto> concertOpt = createConcertService.getConcertById(id, baseImageUrl);
        return concertOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 콘서트 생성
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ConcertResponseDto> createConcert(
            @Valid @RequestPart("concertRequest") CreateConcertRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws Exception {
        Long adminId = userContext.getCurrentUserId();
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("인증된 관리자를 찾을 수 없습니다."));

        Concert concert = createConcertService.createConcert(
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
                request.concertTag(),
                admin,
                request.concertHallId(),
                image
        );

        ConcertResponseDto responseDto = ConcertResponseDto.from(concert, baseImageUrl);
        return ResponseEntity.created(URI.create("/api/concerts/" + concert.getId())).body(responseDto);
    }

    // 콘서트 수정
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ConcertResponseDto> updateConcert(
            @PathVariable Long id,
            @Valid @RequestPart("concertRequest") CreateConcertRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws Exception {
        Long adminId = userContext.getCurrentUserId();
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("인증된 관리자를 찾을 수 없습니다."));

        Optional<Concert> updatedConcertOpt = createConcertService.updateConcert(
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
                request.concertTag(),
                admin,
                request.concertHallId(),
                image
        );

        return updatedConcertOpt
                .map(concert -> ResponseEntity.ok(ConcertResponseDto.from(concert, baseImageUrl)))
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
            String concertTag,
            Long concertHallId
    ) {}
}
