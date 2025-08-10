package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    private String location;

    @Column(name = "location_x", precision = 10, scale = 3)
    private BigDecimal locationX;

    @Column(name = "location_y", precision = 10, scale = 3)
    private BigDecimal locationY;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDateTime reservationStartDate;
    private LocalDateTime reservationEndDate;

    private String price;
    private int rating;

    private int limitAge;

    private int durationTime;

    private String concertTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<Images> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_hall_id")
    private ConcertHall concertHall;

    @OneToOne(mappedBy = "concert", cascade = CascadeType.ALL)
    private ConcertSeatMap concertSeatMap;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<ConcertSchedule> concertSchedules;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<Cast> casts;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static Concert create(
            String title,
            String description,
            String location,
            BigDecimal locationX,
            BigDecimal locationY,
            LocalDate startDate,
            LocalDate endDate,
            LocalDateTime reservationStartDate,
            LocalDateTime reservationEndDate,
            String price,
            int rating,
            int limitAge,
            int durationTime,
            String concertTag,
            Admin admin,
            ConcertHall concertHall
    ) {
        return Concert.builder()
                .title(title)
                .description(description)
                .location(location)
                .locationX(locationX)
                .locationY(locationY)
                .startDate(startDate)
                .endDate(endDate)
                .reservationStartDate(reservationStartDate)
                .reservationEndDate(reservationEndDate)
                .price(price)
                .rating(rating)
                .limitAge(limitAge)
                .durationTime(durationTime)
                .concertTag(concertTag)
                .admin(admin)
                .concertHall(concertHall)
                .build();
    }

    public void update(
            String title,
            String description,
            String location,
            BigDecimal locationX,
            BigDecimal locationY,
            LocalDate startDate,
            LocalDate endDate,
            LocalDateTime reservationStartDate,
            LocalDateTime reservationEndDate,
            String price,
            int rating,
            int limitAge,
            int durationTime,
            String concertTag,
            Admin admin,
            ConcertHall concertHall
    ) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.locationX = locationX;
        this.locationY = locationY;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        this.price = price;
        this.rating = rating;
        this.limitAge = limitAge;
        this.durationTime = durationTime;
        this.concertTag = concertTag;
        this.admin = admin;
        this.concertHall = concertHall;
    }

    public void deleteLogical() {
        this.deletedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    protected void onUpdateTimestamp() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        } else {
            updatedAt = LocalDateTime.now();
        }
    }
}
