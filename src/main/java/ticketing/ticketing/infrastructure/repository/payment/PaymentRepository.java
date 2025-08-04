package ticketing.ticketing.infrastructure.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
