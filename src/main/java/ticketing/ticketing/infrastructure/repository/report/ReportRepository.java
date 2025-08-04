package ticketing.ticketing.infrastructure.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
