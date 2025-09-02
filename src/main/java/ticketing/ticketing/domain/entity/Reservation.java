package ticketing.ticketing.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ticketing.ticketing.domain.enums.ReservationState;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_schedule_id")
    private ConcertSchedule concertSchedule;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ConcertSeat> concertSeats;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_address_id")
    private DeliveryAddress deliveryAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @JsonIgnore
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private ReservationState state;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdateTimestamp() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        } else {
            updatedAt = LocalDateTime.now();
        }
    }
    @PreRemove
    private void deleteLogical() {
        this.deletedAt = LocalDateTime.now();
    }
    public static Reservation create(User user,
                                     ConcertSchedule concertSchedule,
                                     List<ConcertSeat> concertSeats,
                                     DeliveryAddress deliveryAddress,
                                     ReservationState state) {
        return Reservation.builder()
                .user(user)
                .concertSchedule(concertSchedule)
                .concertSeats(concertSeats)
                .deliveryAddress(deliveryAddress)
                .state(state)
                .build();
    }

    public void updateToPayment(Payment payment) {
        this.payment = payment;
    }
}