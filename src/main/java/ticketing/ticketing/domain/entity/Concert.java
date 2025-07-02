package ticketing.ticketing.domain.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    //콘서트 주소지
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
    //소요 시간
    private int durationTime;

    /*@ElementCollection(targetClass = ConcertTag.class)
    @Enumerated(EnumType.STRING)
    private Set<ConcertTag> concertTag;*/
    private String concertTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<Images> images;

    @ManyToOne
    @JoinColumn(name = "concert_hall_id")
    private ConcertHall concertHall;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
