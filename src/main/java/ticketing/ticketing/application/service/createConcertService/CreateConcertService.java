package ticketing.ticketing.application.service.createConcertService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Concert;
import ticketing.ticketing.domain.entity.ConcertHall;
import ticketing.ticketing.infrastructure.repository.createConcert.CreateConcertRepository;
import ticketing.ticketing.infrastructure.repository.consertHall.ConcertHallRepository;
import ticketing.ticketing.application.dto.concertResponseDto.ConcertResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateConcertService {

    private final CreateConcertRepository createConcertRepository;
    private final ConcertHallRepository concertHallRepository;

    // 전체 콘서트 조회
    public List<ConcertResponseDto> getAllConcerts(String baseImageUrl) {
        List<Concert> concerts = createConcertRepository.findAll();
        return concerts.stream()
                .map(c -> ConcertResponseDto.from(c, baseImageUrl))
                .collect(Collectors.toList());
    }

    // 단일 콘서트 조회
    public Optional<ConcertResponseDto> getConcertById(Long id, String baseImageUrl) {
        return createConcertRepository.findById(id)
                .map(c -> ConcertResponseDto.from(c, baseImageUrl));
    }

    // 콘서트 등록
    @Transactional
    public Concert createConcert(
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
            Admin admin,
            Long concertHallId,
            MultipartFile image
    ) throws Exception {

        ConcertHall concertHall = concertHallRepository.findById(concertHallId)
                .orElseThrow(() -> new IllegalArgumentException("ConcertHall not found with id: " + concertHallId));

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
                rating,
                limitAge,
                durationTime,
                concertTag,
                admin,
                concertHall
        );

        return createConcertRepository.save(concert);
    }

    // 콘서트 수정
    @Transactional
    public Optional<Concert> updateConcert(
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
            String concertTag,
            Admin admin,
            Long concertHallId,
            MultipartFile image
    ) throws Exception {

        ConcertHall concertHall = concertHallRepository.findById(concertHallId)
                .orElseThrow(() -> new IllegalArgumentException("ConcertHall not found with id: " + concertHallId));

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
                    concertTag,
                    admin,
                    concertHall
            );

            return concert;
        });
    }

    // 콘서트 삭제
    @Transactional
    public boolean deleteConcert(Long id) {
        return createConcertRepository.findById(id).map(concert -> {
            concert.deleteLogical();
            createConcertRepository.save(concert);
            return true;
        }).orElse(false);
    }

    // 콘서트 직접 저장 (엔티티 사용 시)
    @Transactional
    public Concert saveConcert(Concert concert) {
        return createConcertRepository.save(concert);
    }
}
