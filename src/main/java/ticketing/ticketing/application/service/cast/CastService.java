package ticketing.ticketing.application.service.cast;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.infrastructure.repository.cast.CastRepository;

@Service
@RequiredArgsConstructor
public class CastService {

    private final CastRepository castRepository;


}
