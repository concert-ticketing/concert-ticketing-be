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

    private String concertHallName;

    @OneToOne(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private ConcertSeatMap concertSeatMap;

    @Builder.Default
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertSchedule> concertSchedules = new ArrayList<>();

    // 좌석 구역 리스트
    @Builder.Default
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertSeatSection> concertSeatSections = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<Cast> casts = new ArrayList<>();

    // ✅ 리뷰 리스트 (Concert 삭제 시 리뷰도 같이 삭제됨)
    @Builder.Default
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

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
            int limitAge,
            int durationTime,
            Admin admin,
            String concertHallName
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
                .limitAge(limitAge)
                .durationTime(durationTime)
                .admin(admin)
                .concertHallName(concertHallName)
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
            String concertHallName
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
        this.concertHallName = concertHallName;
    }

    public void deleteLogical() {
        this.deletedAt = LocalDateTime.now();
    }

    // 이미지 추가
    public void addImage(String filename, ImagesRole role) {
        Images image = Images.create(filename, role, this);
        this.images.add(image);
    }

    // 공연회차 추가
    public void addSchedule(ConcertSchedule schedule) {
        schedule.setConcert(this);
        this.concertSchedules.add(schedule);
    }

    // 좌석 구역 추가
    public void addSeatSection(ConcertSeatSection section) {
        section.setConcert(this);
        this.concertSeatSections.add(section);
    }

    // ✅ 리뷰 추가
    public void addReview(Review review) {
        review.setConcert(this);
        this.reviews.add(review);
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
