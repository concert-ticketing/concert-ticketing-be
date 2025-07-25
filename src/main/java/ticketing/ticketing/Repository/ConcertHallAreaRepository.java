package ticketing.ticketing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.ConcertHallArea;

public interface ConcertHallAreaRepository extends JpaRepository<ConcertHallArea, Long> {
}
