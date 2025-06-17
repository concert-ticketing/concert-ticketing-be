package ticketing.ticketing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketing.ticketing.domain.entity.Inquiry;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
