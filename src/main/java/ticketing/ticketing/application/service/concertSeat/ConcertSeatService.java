package ticketing.ticketing.application.service.concertSeat;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.domain.entity.ConcertSeat;
import ticketing.ticketing.domain.entity.SeatReservation;
import ticketing.ticketing.infrastructure.repository.concertSeat.ConcertSeatRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertSeatService {

    private final ConcertSeatRepository concertSeatRepository;

    public List<ConcertSeat> findSeatReservationsByConcertSeatIds(List<Long> concertSeatIds) {
        List<ConcertSeat> seats = concertSeatRepository.findByConcertSeatIds(concertSeatIds);
        if (seats == null || seats.isEmpty()) {
            throw new EntityNotFoundException("해당 콘서트 좌석 예약 정보를 찾을 수 없습니다.");
        }
        return seats;
    }
}
