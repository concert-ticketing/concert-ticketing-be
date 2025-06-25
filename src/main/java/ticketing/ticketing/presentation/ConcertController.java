package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ticketing.ticketing.application.dto.concertDto.ConcertMainPageAddThumbNailReadResponse;
import ticketing.ticketing.application.service.concert.ConcertService;

import java.util.List;


@RestController
@RequestMapping("/api/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;


    @GetMapping("/main-list")
    public ResponseEntity<List<ConcertMainPageAddThumbNailReadResponse>> getConcerts(@RequestParam int size ,@RequestParam(required = false) Long lastId) {
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
    public ResponseEntity<List<ConcertMainPageAddThumbNailReadResponse>> getHighRatingConcert(@RequestParam int size ,@RequestParam(required = false) Long lastId) {
        List <ConcertMainPageAddThumbNailReadResponse> mainPageList;
        if (lastId != null) {
            mainPageList = concertService.getHighRatingConcertListAddLastId(size,lastId);
            if (mainPageList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        }
        else {
            mainPageList = concertService.getHighRatingConcertList(size);
        }

        return ResponseEntity.ok(mainPageList);
    }


/*        @GetMapping("/{id}")
    public ResponseEntity<ConcertDetailPageReadResponse> getConcertDetail(@PathVariable Long id) {
        return null;
    }*/
}
