package ticketing.ticketing.infrastructure.repository.inquiry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketing.ticketing.domain.entity.Inquiry;
import ticketing.ticketing.domain.enums.InquiryStatus;

import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    Page<Inquiry> findByUserId(Long userId, Pageable pageable);

    Optional<Inquiry> findByIdAndUserId(Long id, Long userId);

    Page<Inquiry> findAll(Pageable pageable);

    Page<Inquiry> findAllByStatus(InquiryStatus status, Pageable pageable);
}
