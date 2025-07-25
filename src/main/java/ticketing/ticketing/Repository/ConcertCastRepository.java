package ticketing.ticketing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.ConcertCast;

public interface ConcertCastRepository extends JpaRepository<ConcertCast, Long> {
}
