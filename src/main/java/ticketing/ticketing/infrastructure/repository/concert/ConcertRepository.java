package ticketing.ticketing.infrastructure.repository.concert;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ticketing.ticketing.application.dto.concertDto.ConcertDetailPageReadResponse;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse;
import ticketing.ticketing.application.dto.concertDto.ConcertMapReadResponse;
import ticketing.ticketing.domain.entity.Concert;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    // last Id가 있는 메인페이지 콘서트 조회
    @Query("SELECT new ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse(c.id, c.title, c.startDate, c.endDate,c.location) " +
            "FROM Concert c " +
            "WHERE c.endDate >= CURRENT_DATE " +
            "AND (c.endDate < (SELECT c2.endDate FROM Concert c2 WHERE c2.id = :lastId) " +
            "OR (c.endDate = (SELECT c2.endDate FROM Concert c2 WHERE c2.id = :lastId) AND c.id < :lastId)) " +
            "ORDER BY c.endDate DESC, c.id DESC")
    List<ConcertMainPageInformationReadResponse> getConcertSearchBySizeAndLastId(Pageable pageable, Long lastId);

    // last Id가 없는 메인페이지 콘서트 조회
    @Query("SELECT new ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse(c.id, c.title, c.startDate, c.endDate,c.concertHall.concertHallName) " +
            "FROM Concert c " +
            "WHERE c.endDate >= CURRENT_DATE " +
            "ORDER BY c.endDate DESC, c.id DESC")
    List<ConcertMainPageInformationReadResponse> getConcertSearchBySize(Pageable pageable);

    @Query("SELECT new ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse(c.id, c.title, c.startDate, c.endDate,c.concertHall.concertHallName,c.rating) " +
            "FROM Concert c " +
            "WHERE c.endDate >= CURRENT_DATE " +
            "ORDER BY c.rating DESC, c.id DESC")
    List<ConcertMainPageInformationReadResponse> getHighRatingConcertSearchBySize(Pageable pageable);

    @Query("SELECT new ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse(c.id, c.title, c.startDate, c.endDate,c.concertHall.concertHallName, c.rating) " +
            "FROM Concert c " +
            "WHERE c.endDate >= CURRENT_DATE " +
            "AND (c.rating < (SELECT c2.rating FROM Concert c2 WHERE c2.id = :lastId) " +
            "OR (c.rating = (SELECT c2.rating FROM Concert c2 WHERE c2.id = :lastId) AND c.id < :lastId)) " +
            "ORDER BY c.rating DESC, c.id DESC")
    List<ConcertMainPageInformationReadResponse> getHighRatingConcertSearchBySizeAndLastId(Pageable pageable, Long lastId);

    // 상세 조회 (concertTag 필드 제외)
    @Query("""
            SELECT new ticketing.ticketing.application.dto.concertDto.ConcertDetailPageReadResponse(
                c.title,
                c.description,
                c.concertHall.concertHallName,
                c.startDate,
                c.endDate,
                c.reservationStartDate,
                c.reservationEndDate,
                c.price,
                c.limitAge,
                c.durationTime
            )
            FROM Concert c
            WHERE c.id = :id
            """)
    ConcertDetailPageReadResponse getConcertById(Long id);

    @Query("SELECT c.rating FROM Concert c WHERE c.id = :id ")
    Integer getConcertRatingById(Long id);

    @Query("SELECT new ticketing.ticketing.application.dto.concertDto.ConcertMapReadResponse(c.locationX,c.locationY,c.location,c.concertHall.concertHallName,c.admin.phone) " +
            "FROM Concert c " +
            "WHERE c.id = :id")
    ConcertMapReadResponse getConcertMapById(Long id);

}
