package ticketing.ticketing.infrastructure.repository.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
