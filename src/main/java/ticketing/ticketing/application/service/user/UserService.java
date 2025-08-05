package ticketing.ticketing.application.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;
import ticketing.ticketing.application.dto.userDto.UserOAuthUpdateRequest;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final Map<String, OAuthProviderService> oauthProviderServiceMap;

    public User findUserById(Long id) {
        log.info("Find user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("유저 정보 없음"));
    }

    public String createOAuthUser(UserOAuthCreateRequest request) {
        String provider = request.getState().toLowerCase();
        OAuthProviderService service = oauthProviderServiceMap.get(provider);
        if (service == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth 제공자: " + provider);
        }
        return service.createOAuth(request);
    }

    public Long updateOAuthUser(UserOAuthUpdateRequest request, Long userId) {
        User user = findUserById(userId);
        user.update(request.getName(),request.getEmail(),request.getNickName(),request.getPhone(),request.getGender(),request.getBirthday());
        return userRepository.save(user).getId();
    }
}

