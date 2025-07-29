package ticketing.ticketing.application.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;
import ticketing.ticketing.application.dto.userDto.UserOAuthTokenReadResponse;
import ticketing.ticketing.application.service.user.oauth.kakao.GetAccessTokenFromKakaoService;
import ticketing.ticketing.application.service.user.oauth.kakao.GetUserInfoFromKakaoService;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;
import ticketing.ticketing.infrastructure.security.JwtUtil;

@Service("kakao")
@RequiredArgsConstructor
public class KakaoOauthService implements OAuthProviderService{

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final GetAccessTokenFromKakaoService getAccessTokenFromKakaoService;
    private final GetUserInfoFromKakaoService getUserInfoFromKakaoService;

    public String createOAuth(UserOAuthCreateRequest request) {
        String code = request.getCode();
        String kakaoAccessToken = getAccessTokenFromKakaoService.getAccessTokenFromKakao(code);
        UserOAuthTokenReadResponse userInfo = getUserInfoFromKakaoService.getUserInfoFromKakao(kakaoAccessToken);
        User user = userRepository.findByUserId(userInfo.getUserId())
                .orElseGet(() -> userRepository.save(User.create(userInfo.getUserId())));
        return jwtUtil.generateToken(user.getId(), "USER");
    }
}
