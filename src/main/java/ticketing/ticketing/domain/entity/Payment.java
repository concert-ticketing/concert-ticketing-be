package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ticketing.ticketing.domain.enums.PaymentState;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long totalPrice;

  @Enumerated(EnumType.STRING)
  private PaymentState state;

  private String paymentType;

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

 public static Payment create(Long totalPrice, PaymentState state, String paymentType) {
   return Payment.builder()
           .totalPrice(totalPrice)
           .state(state)
           .paymentType(paymentType)
           .build();
 }


}