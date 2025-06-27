package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PROTECTED)
@Getter
public class ConcertHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //콘서트장 이름
    private String concertHallName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "concertHall")
    private List<Concert> concert;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
