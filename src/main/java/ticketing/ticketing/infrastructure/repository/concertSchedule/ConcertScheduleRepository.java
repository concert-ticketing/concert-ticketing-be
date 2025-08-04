package ticketing.ticketing.infrastructure.repository.concertSchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.ConcertSchedule;

public interface ConcertScheduleRepository extends JpaRepository<ConcertSchedule, Long> {
}
