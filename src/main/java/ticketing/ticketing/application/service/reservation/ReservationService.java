package ticketing.ticketing.application.service.reservation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.reservationDto.ReservationCreateRequest;
import ticketing.ticketing.application.dto.reservationDto.ReservationReadResponse;
import ticketing.ticketing.application.service.concertSchedule.ConcertScheduleService;
import ticketing.ticketing.application.service.concertSeat.ConcertSeatService;
import ticketing.ticketing.application.service.deliveryAddress.DeliveryAddressService;
import ticketing.ticketing.application.service.seatReservation.SeatReservationService;
import ticketing.ticketing.application.service.user.UserService;
import ticketing.ticketing.domain.entity.*;
import ticketing.ticketing.domain.enums.ReservationState;
import ticketing.ticketing.domain.enums.SeatReservationState;
import ticketing.ticketing.infrastructure.repository.reservation.ReservationRepository;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;
import ticketing.ticketing.infrastructure.security.UserContext;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserContext userContext;
    private final UserService userService;
    private final ConcertScheduleService concertScheduleService;
    private final SeatReservationService seatReservationService;
    private final ConcertSeatService concertSeatService;
    private final DeliveryAddressService deliveryAddressService;
    private final UserRepository userRepository;

    @Transactional
    public ReservationReadResponse createReservation(ReservationCreateRequest request) {
        // 1. 사용자 조회
        Long userId = userContext.getCurrentUserId();
        User user = userService.findUserById(userId);

        // 2. 콘서트 스케줄 조회
        ConcertSchedule schedule = concertScheduleService.getFindScheduleById(request.getConcertScheduleId());

        // 3. 좌석 예약 상태 검증
        List<ConcertSeat> seatReservations = concertSeatService.findSeatReservationsByConcertSeatIds(request.getSeatReservationIds());
        validateSeatAvailability(seatReservations);

        // 4. 배송 주소 생성
        DeliveryAddress deliveryAddress = deliveryAddressService.createDeliveryAddress(request.getDeliveryAddress());

        // 5. 예약 상태 변환
        ReservationState reservationState = parseReservationState(request.getState());
        if(reservationState == null) {
            reservationState = ReservationState.PENDING;
        }

        // 6. 예약 생성
        Reservation reservation = Reservation.create(user, schedule, seatReservations, deliveryAddress, reservationState);

        // 7. 좌석에 예약 ID 설정 (중요!)
        Reservation savedReservation = reservationRepository.save(reservation);
        seatReservationService.updateSeatReservationStatus(seatReservations, savedReservation);

        // 8. DTO로 변환해서 반환
        return ReservationReadResponse.from(savedReservation);
    }
    private void validateSeatAvailability(List<ConcertSeat> concertSeats) {
            // 이미 예약된 좌석만 필터링 (UNAVAILABLE)
            List<ConcertSeat> alreadyReserved = concertSeats.stream()
                    .filter(seat -> seat.getSeatReservationState() == SeatReservationState.UNAVAILABLE)
                    .toList();

            if (!alreadyReserved.isEmpty()) {
                List<Long> reservedSeatIds = alreadyReserved.stream()
                        .map(ConcertSeat::getId)
                        .toList();
                throw new IllegalStateException("이미 예약된 좌석이 있습니다: " + reservedSeatIds);
            }
        }


    private ReservationState parseReservationState(String state) {
        try {
            return ReservationState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("유효하지 않은 예약 상태입니다: " + state);
        }
    }

    @Transactional
    public void updateReservationByPaymentInfo(Payment payment, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다: " + reservationId));

        reservation.updateToPayment(payment);
        reservationRepository.save(reservation);
    }

    public ResponseEntity<List<ReservationReadResponse>> getAllReservationsInfo() {
        Long userId = userContext.getCurrentUserId();
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        List<Reservation> reservations = reservationRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found for user id: " + userId));

        List<ReservationReadResponse> responseList = ReservationReadResponse.from(reservations);
        return ResponseEntity.ok(responseList);
    }
}
