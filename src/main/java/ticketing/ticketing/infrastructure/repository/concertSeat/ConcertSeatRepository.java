package ticketing.ticketing.infrastructure.repository.concertSeat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ticketing.ticketing.domain.entity.ConcertSeat;
import ticketing.ticketing.domain.entity.SeatReservation;

import java.util.List;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> {

    @Query("SELECT c FROM ConcertSeat c WHERE c.id IN :concertSeatIdList")
    List<ConcertSeat> findByConcertSeatIds(@Param("concertSeatIdList") List<Long> concertSeatIdList);
}
