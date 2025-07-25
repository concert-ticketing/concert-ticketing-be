package ticketing.ticketing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.ConcertHall;

public interface ConcertHallRepository extends JpaRepository<ConcertHall, Long> {
}
