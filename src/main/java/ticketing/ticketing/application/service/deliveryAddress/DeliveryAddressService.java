package ticketing.ticketing.application.service.deliveryAddress;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.deliveryAddressDto.DeliveryAddressCreateRequest;
import ticketing.ticketing.domain.entity.DeliveryAddress;
import ticketing.ticketing.infrastructure.repository.deliveryAddress.DeliveryAddressRepository;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository deliveryAddressRepository;

    public DeliveryAddress createDeliveryAddress(DeliveryAddressCreateRequest request) {

        DeliveryAddress deliveryAddress = DeliveryAddress.create(
                request.getName(),
                request.getPhone(),
                request.getAddress(),
                request.getDetailAddress()
        );

        return deliveryAddressRepository.save(deliveryAddress);
    }
}
