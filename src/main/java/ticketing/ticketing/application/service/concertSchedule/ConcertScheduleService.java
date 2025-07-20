package ticketing.ticketing.application.service.concertSchedule;

import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ticketing.ticketing.domain.entity.ConcertSchedule;
import ticketing.ticketing.infrastructure.repository.concertSchedule.ConcertScheduleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConcertScheduleService {

    private final ConcertScheduleRepository concertScheduleRepository;

    public ConcertSchedule getFindScheduleById(Long id) {
        Optional<ConcertSchedule> scheduleOpt = concertScheduleRepository.findById(id);
        return scheduleOpt.orElseThrow(() -> new EntityNotFoundException("콘서트 일정 없음"));
    }
}
