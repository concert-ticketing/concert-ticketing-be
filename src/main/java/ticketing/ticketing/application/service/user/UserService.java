package ticketing.ticketing.application.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.userDto.OAuthLoginResponse;
import ticketing.ticketing.application.dto.userDto.UserInfoReadResponse;
import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;
import ticketing.ticketing.application.dto.userDto.UserOAuthUpdateRequest;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final Map<String, OAuthProviderService> oauthProviderServiceMap;
    private final WebInvocationPrivilegeEvaluator privilegeEvaluator;

    public User findUserById(Long id) {
        log.info("Find user by id: {}", id);
        Optional<User> scheduleOpt = userRepository.findById(id);
        return scheduleOpt.orElseThrow(() -> new EntityNotFoundException("유저 정보 없음"));
    }

    public Optional<User> findByUserId(String userId){
        Optional<User> User = userRepository.findByUserId(userId);
        return User;

    }

    public OAuthLoginResponse createOAuthUser(UserOAuthCreateRequest request) {
        String rawState = request.getState();
        String provider = rawState.split("_")[0].toLowerCase();
        OAuthProviderService service = oauthProviderServiceMap.get(provider.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth 제공자: " + provider);
        }
        return service.createOAuth(request);
    }

    public Long updateOAuthUser(UserOAuthUpdateRequest request, Long userId) {
        User user = findUserById(userId);
        user.update(request.getName(),request.getEmail(),request.getNickName(),request.getPhone(),request.getGender() ,request.getBirthday());
        return userRepository.save(user).getId();
    }

    public Page<UserInfoReadResponse> getUserInfo(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable); // 페이징된 유저 목록 조회

        return users.map(user -> UserInfoReadResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .nickName(user.getNickName())
                .gender(user.getGender())
                .state(user.getState())
                .birthday(user.getBirthday())
                .build());
    }

}
