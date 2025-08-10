package ticketing.ticketing.infrastructure.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Inquiry;
import ticketing.ticketing.domain.enums.AdminRole;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdminId(String adminId);

    Optional<Admin> findByIdAndRole(Long id, AdminRole role);

    Page<Admin> findByRole(AdminRole CONCERT_ADMIN, Pageable pageable);





}
