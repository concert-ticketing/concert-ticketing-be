package ticketing.ticketing.application.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ticketing.ticketing.domain.entity.ConcertSchedule;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public void saveOrUpdateUser(OAuth2User user) {
        String email = (String) user.getAttributes().get("email");
        // 저장 또는 업데이트 로직
    }

    public User findUserById(Long id) {
        Optional<User> scheduleOpt = userRepository.findById(id);
        return scheduleOpt.orElseThrow(() -> new EntityNotFoundException("콘서트 일정 없음"));
    }
}
