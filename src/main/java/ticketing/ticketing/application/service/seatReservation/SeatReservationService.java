package ticketing.ticketing.application.service.seatReservation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.domain.entity.Reservation;
import ticketing.ticketing.domain.entity.SeatReservation;
import ticketing.ticketing.infrastructure.seatReservation.SeatReservationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatReservationService {

    private final SeatReservationRepository seatReservationRepository;

    public List<SeatReservation> findSeatReservationById(List<Long> id) {
        List<SeatReservation> seats = seatReservationRepository.findByListSeatId(id);
        if (seats == null || seats.isEmpty()) {
            throw new EntityNotFoundException("좌석 예약 정보를 찾을 수 없습니다.");
        }
        return seats;
    }

    @Transactional
    public List<Long> updateSeatReservationStatus(List<SeatReservation> seatReservations, Reservation reservation) {

        List<Long> updatedSeatIds = new ArrayList<>();

        for (SeatReservation seatReservation : seatReservations) {
            seatReservation.updateReservation(reservation);
            updatedSeatIds.add(seatReservation.getId());
        }

        // 3. 배치로 저장 (성능 최적화)
        seatReservationRepository.saveAll(seatReservations);

        // 4. 업데이트된 좌석 ID들 반환
        return updatedSeatIds;
    }
}
