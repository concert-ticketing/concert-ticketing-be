package ticketing.ticketing.application.service.user;

import ticketing.ticketing.application.dto.userDto.OAuthLoginResponse;
import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;

public interface OAuthProviderService {
    OAuthLoginResponse createOAuth(UserOAuthCreateRequest request);
}
