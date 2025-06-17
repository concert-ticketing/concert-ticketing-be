package ticketing.ticketing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketing.ticketing.domain.entity.InquiryFile;

@Repository
public interface InquiryFileRepository extends JpaRepository<InquiryFile, Long> {
}

