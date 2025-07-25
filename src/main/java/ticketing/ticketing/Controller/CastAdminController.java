package ticketing.ticketing.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.service.cast.CastService;
import ticketing.ticketing.domain.entity.Cast;

@RestController
@RequestMapping("/admin/casts")
@RequiredArgsConstructor
public class CastAdminController {

    private final CastService castService;

    @GetMapping
    public ResponseEntity<Page<Cast>> getCasts(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) String name) {
        Page<Cast> casts = castService.getCasts(pageable, name);
        return ResponseEntity.ok(casts);
    }
}
