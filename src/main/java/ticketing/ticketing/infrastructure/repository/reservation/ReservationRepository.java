package ticketing.ticketing.infrastructure.repository.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<List<Reservation>> findByUserId (Long userId);
}
