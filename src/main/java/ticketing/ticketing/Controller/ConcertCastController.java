package ticketing.ticketing.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.Service.ConcertCastService;
import ticketing.ticketing.domain.entity.ConcertCast;
import ticketing.ticketing.domain.enums.CastRole;

@RestController
@RequestMapping("/admin/concert-casts")
@RequiredArgsConstructor
public class ConcertCastController {

    private final ConcertCastService concertCastService;

    @PostMapping
    public ResponseEntity<ConcertCast> createConcertCast(
            @RequestParam Long scheduleId,
            @RequestParam Long castId,
            @RequestParam CastRole role) {

        ConcertCast concertCast = concertCastService.addConcertCast(scheduleId, castId, role);
        return ResponseEntity.ok(concertCast);
    }
}
