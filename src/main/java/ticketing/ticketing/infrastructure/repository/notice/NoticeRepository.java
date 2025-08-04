package ticketing.ticketing.infrastructure.repository.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}