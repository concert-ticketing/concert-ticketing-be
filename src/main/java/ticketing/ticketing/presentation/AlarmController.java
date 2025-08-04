package ticketing.ticketing.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.alarmResponseDto.AlarmResponseDto;
import ticketing.ticketing.application.service.alarm.AlarmService;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;

@RestController
@RequestMapping("/mypage/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<AlarmResponseDto>> getMyAlarms(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        PageRequest pageable = PageRequest.of(page, size);
        Page<AlarmResponseDto> alarms = alarmService.getUserAlarms(user, pageable);

        return ResponseEntity.ok(alarms);
    }
}
