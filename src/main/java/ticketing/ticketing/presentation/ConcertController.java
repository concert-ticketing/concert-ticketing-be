package ticketing.ticketing.presentation;

import jakarta.servlet.ServletConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ticketing.ticketing.application.dto.concertDto.ConcertDetailPageReadResponse;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageAddThumbNailReadResponse;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse;
import ticketing.ticketing.application.dto.concertDto.ConcertMapReadResponse;
import ticketing.ticketing.application.service.concert.ConcertService;
import ticketing.ticketing.domain.entity.Concert;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;
    private final ServletConfig servletConfig;


    @GetMapping("/main-list")
    public ResponseEntity<List<ConcertMainPageAddThumbNailReadResponse>> getConcerts(@RequestParam int size, @RequestParam(required = false) Long lastId) {
        List<ConcertMainPageAddThumbNailReadResponse> mainPageList;
        if (lastId != null) {
            mainPageList = concertService.getMainPageSearchConcertAddLastId(size, lastId);
            if (mainPageList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        } else {
            mainPageList = concertService.getMainPageSearchConcert(size);
        }
        return ResponseEntity.ok(mainPageList);
    }

    @GetMapping("/high-rating")
    public ResponseEntity<List<ConcertMainPageAddThumbNailReadResponse>> getHighRatingConcert(@RequestParam int size, @RequestParam(required = false) Long lastId) {
        List<ConcertMainPageAddThumbNailReadResponse> mainPageList;
        if (lastId != null) {
            mainPageList = concertService.getHighRatingConcertListAddLastId(size, lastId);
            if (mainPageList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        } else {
            mainPageList = concertService.getHighRatingConcertList(size);
        }

        return ResponseEntity.ok(mainPageList);
    }


    @GetMapping("/")
    public ResponseEntity<ConcertDetailPageReadResponse> getConcertDetail(@RequestParam Long id) {
        ConcertDetailPageReadResponse detailPage = concertService.getConcertDetailPageById(id);
        if (detailPage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detailPage);
    }

    @GetMapping("/map")
    public ResponseEntity<ConcertMapReadResponse> getConcertMap(@RequestParam Long id) {
        ConcertMapReadResponse map = concertService.getConcertMapById(id);
        if(map == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(map);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ConcertMainPageAddThumbNailReadResponse>> searchConcerts(@RequestParam int size
            , @RequestParam(required = false) Long lastId, @RequestParam(required = false) String title) {
        List<Concert> concerts = concertService.searchConcertsByTitle(title, size, lastId);

        List<ConcertMainPageAddThumbNailReadResponse> response = concerts.stream()
                .map(ConcertMainPageAddThumbNailReadResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }
}
