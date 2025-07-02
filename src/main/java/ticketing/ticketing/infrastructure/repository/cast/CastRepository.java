package ticketing.ticketing.infrastructure.repository.cast;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Cast;

public interface CastRepository extends JpaRepository<Cast, Long> {


}
