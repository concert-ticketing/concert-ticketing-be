package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ticketing.ticketing.domain.enums.ImagesRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Builder.Default
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Images> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_hall_id")
    private ConcertHall concertHall;

    @OneToOne(mappedBy = "concert", cascade = CascadeType.ALL)
    private ConcertSeatMap concertSeatMap;

    @Builder.Default
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertSchedule> concertSchedules = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<Cast> casts = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    // 생성 메서드
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
                .admin(admin)
                .concertHall(concertHall)
                .build();
    }

    // 수정 메서드
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
        this.admin = admin;
        this.concertHall = concertHall;
    }

    public void deleteLogical() {
        this.deletedAt = LocalDateTime.now();
    }

    // 이미지 추가 메서드
    public void addImage(String filename, ImagesRole role) {
        Images image = Images.create(filename, role, this);
        this.images.add(image);
    }

    // 공연회차 추가 메서드
    public void addSchedule(ConcertSchedule schedule) {
        schedule.setConcert(this);
        this.concertSchedules.add(schedule);
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
