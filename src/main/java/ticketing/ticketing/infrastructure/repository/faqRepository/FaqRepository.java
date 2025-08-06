package ticketing.ticketing.infrastructure.repository.faqRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Faq;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    List<Faq> findAllByDeletedAtIsNull();
}
