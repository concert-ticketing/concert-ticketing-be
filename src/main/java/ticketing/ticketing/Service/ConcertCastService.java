package ticketing.ticketing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.ticketing.infrastructure.repository.cast.CastRepository;
import ticketing.ticketing.Repository.ConcertCastRepository;
import ticketing.ticketing.Repository.ConcertScheduleRepository;
import ticketing.ticketing.domain.entity.Cast;
import ticketing.ticketing.domain.entity.ConcertCast;
import ticketing.ticketing.domain.entity.ConcertSchedule;
import ticketing.ticketing.domain.enums.CastRole;

@Service
@RequiredArgsConstructor
public class ConcertCastService {

    private final ConcertCastRepository concertCastRepository;
    private final ConcertScheduleRepository concertScheduleRepository;
    private final CastRepository castRepository;

    @Transactional
    public ConcertCast addConcertCast(Long scheduleId, Long castId, CastRole role) {
        ConcertSchedule schedule = concertScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid concert schedule ID"));

        Cast cast = castRepository.findById(castId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cast ID"));

        ConcertCast concertCast = ConcertCast.create(schedule, cast, role);

        return concertCastRepository.save(concertCast);
    }
}
