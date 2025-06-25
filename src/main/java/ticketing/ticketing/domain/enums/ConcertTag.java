package ticketing.ticketing.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ConcertTag {
    LARGE("대형"),
    MEDIUM("중형"),
    SMALL("소형"),
    INDOOR("실내"),
    OUTDOOR("야외"),
    MIXED("복합"),
    CLASSIC("클래식"),
    POP("팝"),
    ROCK("록"),
    JAZZ("재즈"),
    MUSICAL("뮤지컬"),
    OPERA("오페라"),
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    DAEJEON("대전");

    private final String label;

    ConcertTag(String label) {
        this.label = label;
    }

    public static ConcertTag fromLabel(String label) {
        return Arrays.stream(values())
                .filter(tag -> tag.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown label: " + label));
    }
}
