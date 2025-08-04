package ticketing.ticketing.infrastructure.repository.alarm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Alarm;
import ticketing.ticketing.domain.entity.User;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findByUser(User user, Pageable pageable);
}
