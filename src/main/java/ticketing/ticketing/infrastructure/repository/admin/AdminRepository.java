package ticketing.ticketing.infrastructure.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.enums.AdminRole;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdminId(String adminId);

    List<Admin> findByRole(AdminRole CONCERT_ADMIN);
}
