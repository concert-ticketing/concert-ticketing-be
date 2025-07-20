package ticketing.ticketing.application.service.payment;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.paymentDto.PaymentCreateRequest;
import ticketing.ticketing.application.dto.paymentDto.PaymentReadResponse;
import ticketing.ticketing.application.service.reservation.ReservationService;
import ticketing.ticketing.domain.entity.Payment;
import ticketing.ticketing.infrastructure.repository.payment.PaymentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationService reservationService;


    @Transactional
    public Payment creatPayment(PaymentCreateRequest request) {
        Payment payment = Payment.create(request.getTotalPrice(),request.getState(),request.getPaymentType());
        Payment savedPayment = paymentRepository.save(payment);
        PaymentReadResponse readResponse = PaymentReadResponse.from(savedPayment);
        return savedPayment;
        //return paymentRepository.save(payment);
    }

    public Payment findPaymentInfo(Long id) {
        Optional<Payment> scheduleOpt = paymentRepository.findById(id);
        return scheduleOpt.orElseThrow(() -> new EntityNotFoundException("결제 내역 없음"));
    }

}
