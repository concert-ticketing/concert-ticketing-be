package ticketing.ticketing.infrastructure.repository.deliveryAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.DeliveryAddress;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
}
