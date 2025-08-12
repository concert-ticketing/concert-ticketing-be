/*
package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.concertHallCreateRequestDto.ConcertHallCreateRequestDto;
import ticketing.ticketing.application.dto.concertHallResponseDto.ConcertHallResponseDto;
import ticketing.ticketing.application.service.concertHall.ConcertHallService;

import java.util.List;

@RestController
@RequestMapping("/admin/concert-halls")
@RequiredArgsConstructor
public class ConcertHallController {

    private final ConcertHallService concertHallService;

    @PostMapping
    public ResponseEntity<Void> createConcertHall(@RequestBody ConcertHallCreateRequestDto requestDto) {
        concertHallService.createConcertHall(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ConcertHallResponseDto>> getAllConcertHalls() {
        return ResponseEntity.ok(concertHallService.getAllConcertHalls());
    }
}*/
