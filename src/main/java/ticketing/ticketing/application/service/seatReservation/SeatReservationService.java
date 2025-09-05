package ticketing.ticketing.application.service.seatReservation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.domain.entity.ConcertSeat;
import ticketing.ticketing.domain.entity.Reservation;
import ticketing.ticketing.domain.entity.SeatReservation;
import ticketing.ticketing.infrastructure.repository.concertSeat.ConcertSeatRepository;
import ticketing.ticketing.infrastructure.repository.seatReservation.SeatReservationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatReservationService {

    private final SeatReservationRepository seatReservationRepository;
    private final ConcertSeatRepository concertSeatRepository;

    public List<SeatReservation> findSeatReservationsByConcertSeatIds(List<Long> concertSeatIds) {
        List<SeatReservation> seats = seatReservationRepository.findByConcertSeatIds(concertSeatIds);
        if (seats == null || seats.isEmpty()) {
            throw new EntityNotFoundException("해당 콘서트 좌석 예약 정보를 찾을 수 없습니다.");
        }
        return seats;
    }

    @Transactional
    public List<Long> updateSeatReservationStatus(List<ConcertSeat> seats, Reservation reservation) {

        List<Long> updatedSeatIds = new ArrayList<>();

        for (ConcertSeat seat : seats) {
            seat.markAsReserved();
            seat.assignToReservation(reservation);
            updatedSeatIds.add(seat.getId());
        }
        concertSeatRepository.saveAll(seats);

        return updatedSeatIds;
    }
}