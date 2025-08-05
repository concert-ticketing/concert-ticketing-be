package ticketing.ticketing.application.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.userDto.OAuthLoginResponse;
import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;
import ticketing.ticketing.application.dto.userDto.UserOAuthTokenReadResponse;
import ticketing.ticketing.application.service.user.oauth.google.GetAccessTokenFromGoogleService;
import ticketing.ticketing.application.service.user.oauth.google.GetUserInfoFromGoogleService;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;
import ticketing.ticketing.infrastructure.security.JwtUtil;

import java.util.Optional;

@Service("google")
@RequiredArgsConstructor
public class GoogleOauthService implements  OAuthProviderService{

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final GetAccessTokenFromGoogleService getAccessTokenFromGoogleService;
    private final GetUserInfoFromGoogleService getUserInfoFromGoogleService;

    public OAuthLoginResponse createOAuth(UserOAuthCreateRequest request) {
        String code = request.getCode();
        String googleAccessToken = getAccessTokenFromGoogleService.getAccessTokenFromGoogle(code);
        UserOAuthTokenReadResponse userInfo = getUserInfoFromGoogleService.getUserInfoFromGoogle(googleAccessToken);

        Optional<User> existingUser = userRepository.findByUserId(userInfo.getUserId());
        boolean isFirst = existingUser.map(user -> user.getUpdatedAt() == null).orElse(true);

        User user = existingUser.orElseGet(() -> userRepository.save(User.create(userInfo.getUserId())));
        String token = jwtUtil.generateToken(user.getId(), "USER");

        return new OAuthLoginResponse(token, isFirst);
    }
}
