package ticketing.ticketing.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSeatMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;
    private String storedFileName;
    private String storedFilePath;

    private LocalDateTime uploadedAt;

    @OneToOne
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    // 정적 팩토리 메서드
    public static ConcertSeatMap create(String originalFileName, String storedFileName, String storedFilePath, Concert concert) {
        ConcertSeatMap seatMap = new ConcertSeatMap();
        seatMap.originalFileName = originalFileName;
        seatMap.storedFileName = storedFileName;
        seatMap.storedFilePath = storedFilePath;
        seatMap.uploadedAt = LocalDateTime.now();
        seatMap.concert = concert;
        return seatMap;
    }
}
