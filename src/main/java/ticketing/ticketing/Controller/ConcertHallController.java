package ticketing.ticketing.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.DTO.ConcertHallCreateRequestDto;
import ticketing.ticketing.DTO.ConcertHallResponseDto;
import ticketing.ticketing.Service.ConcertHallService;

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
}