package ticketing.ticketing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Seats;

public interface SeatsRepository extends JpaRepository<Seats, Long> {
}
