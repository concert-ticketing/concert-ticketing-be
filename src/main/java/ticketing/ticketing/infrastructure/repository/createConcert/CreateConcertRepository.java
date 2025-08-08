package ticketing.ticketing.infrastructure.repository.createConcert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketing.ticketing.domain.entity.Concert;

@Repository
public interface CreateConcertRepository extends JpaRepository<Concert, Long> {
}
