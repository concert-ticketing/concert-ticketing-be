package ticketing.ticketing.infrastructure;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse;
import ticketing.ticketing.domain.entity.Concert;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Integer> {

    @Query("SELECT new ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse(c.id, c.title, c.startDate, c.endDate) " +
            "FROM Concert c " +
            "WHERE c.endDate >= CURRENT_DATE " +
            "AND (c.endDate < (SELECT c2.endDate FROM Concert c2 WHERE c2.id = :lastId) " +
            "OR (c.endDate = (SELECT c2.endDate FROM Concert c2 WHERE c2.id = :lastId) AND c.id < :lastId)) " +
            "ORDER BY c.endDate DESC, c.id DESC")
    List<ConcertMainPageInformationReadResponse> getConcertSearchBySizeAndLastId(Pageable pageable, Long lastId);


    @Query("SELECT new ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse(c.id, c.title, c.startDate, c.endDate) " +
            "FROM Concert c " +
            "WHERE c.endDate >= CURRENT_DATE " +
            "ORDER BY c.endDate DESC, c.id DESC")
    List<ConcertMainPageInformationReadResponse> getConcertSearchBySize(Pageable pageable);
}
