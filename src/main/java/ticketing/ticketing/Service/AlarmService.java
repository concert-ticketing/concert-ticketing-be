package ticketing.ticketing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ticketing.ticketing.DTO.AlarmResponseDto;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.Repository.AlarmRepository;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public Page<AlarmResponseDto> getUserAlarms(User user, Pageable pageable) {
        return alarmRepository.findByUser(user, pageable)
                .map(AlarmResponseDto::from);
    }
}
