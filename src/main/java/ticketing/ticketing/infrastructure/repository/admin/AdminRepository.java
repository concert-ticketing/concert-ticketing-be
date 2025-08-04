package ticketing.ticketing.infrastructure.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
