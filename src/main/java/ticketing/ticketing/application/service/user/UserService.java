package ticketing.ticketing.application.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Map<String, OAuthProviderService> oauthProviderServiceMap;
    private final WebInvocationPrivilegeEvaluator privilegeEvaluator;

    public User findUserById(Long id) {
        Optional<User> scheduleOpt = userRepository.findById(id);
        return scheduleOpt.orElseThrow(() -> new EntityNotFoundException("콘서트 일정 없음"));
    }

    public String createOAuthUser(UserOAuthCreateRequest request) {
        String provider = request.getState().toLowerCase();
        OAuthProviderService service = oauthProviderServiceMap.get(provider.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth 제공자: " + provider);
        }
        return service.createOAuth(request);
    }
}
