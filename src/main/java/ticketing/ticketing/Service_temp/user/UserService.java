package ticketing.ticketing.Service_temp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements ticketing.ticketing.Service_temp.user.IUserService{
    private final UserRepository userRepository;

    public void saveOrUpdateUser(OAuth2User user) {
        String email = (String) user.getAttributes().get("email");
        // 저장 또는 업데이트 로직
    }
}
