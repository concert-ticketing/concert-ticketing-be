package ticketing.ticketing.application.service.cast;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ticketing.ticketing.domain.entity.Cast;
import ticketing.ticketing.infrastructure.repository.cast.CastRepository;

@Service
@RequiredArgsConstructor
public class CastService {

    private final CastRepository castRepository;

    public Page<Cast> getCasts(Pageable pageable, String name) {
        if (name == null || name.isBlank()) {
            return castRepository.findAll(pageable);
        }
        return castRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
