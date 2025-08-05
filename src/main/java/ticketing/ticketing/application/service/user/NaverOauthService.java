package ticketing.ticketing.application.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.userDto.OAuthLoginResponse;
import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;
import ticketing.ticketing.application.dto.userDto.UserOAuthTokenReadResponse;
import ticketing.ticketing.application.service.user.oauth.naver.GetAccessTokenFromNaverService;
import ticketing.ticketing.application.service.user.oauth.naver.GetUserInfoFromNaverService;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;
import ticketing.ticketing.infrastructure.security.JwtUtil;

import java.util.Optional;

@Service("naver")
@RequiredArgsConstructor
public class NaverOauthService implements OAuthProviderService{
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final GetAccessTokenFromNaverService getAccessTokenFromNaverService;
    private final GetUserInfoFromNaverService getUserInfoFromNaverService;

    public OAuthLoginResponse createOAuth(UserOAuthCreateRequest request) {
        String code = request.getCode();
        String state = request.getState();
        String naverAccessToken = getAccessTokenFromNaverService.getAccessTokenFromNaver(code,state);
        UserOAuthTokenReadResponse userInfo = getUserInfoFromNaverService.getUserInfoFromNaver(naverAccessToken);

        Optional<User> existingUser = userRepository.findByUserId(userInfo.getUserId());

        boolean isFirst = existingUser.isEmpty();

        User user = existingUser.orElseGet(() -> userRepository.save(User.create(userInfo.getUserId())));

        String token = jwtUtil.generateToken(user.getId(), "USER");

        return new OAuthLoginResponse(token, isFirst);
    }
}
