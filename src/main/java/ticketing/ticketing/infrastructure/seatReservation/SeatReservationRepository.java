package ticketing.ticketing.infrastructure.seatReservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ticketing.ticketing.domain.entity.SeatReservation;

import java.util.List;
import java.util.UUID;

public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {


    @Query("SELECT s FROM SeatReservation s WHERE s.id IN :concertSeatIdList")
    List<SeatReservation> findByConcertSeatIds(@Param("concertSeatIdList") List<Long> concertSeatIdList);
}
