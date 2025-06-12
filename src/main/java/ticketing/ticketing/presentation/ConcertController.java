package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageAddThumbNailReadResponse;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse;
import ticketing.ticketing.application.service.concert.ConcertService;

import java.util.List;


@RestController
@RequestMapping("/api/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;
    @GetMapping("/main-list")
    public ResponseEntity<List<ConcertMainPageAddThumbNailReadResponse>> getConcerts(@RequestParam(required = false) Long lastId) {
        List<ConcertMainPageAddThumbNailReadResponse> mainPageList = concertService.getMainPageSearchConcert(lastId);
        return ResponseEntity.ok(mainPageList);
    }
}
