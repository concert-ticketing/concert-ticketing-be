package ticketing.ticketing.application.service.user;

import ticketing.ticketing.application.dto.userDto.UserOAuthCreateRequest;

public interface OAuthProviderService {
    String createOAuth(UserOAuthCreateRequest request);
}
