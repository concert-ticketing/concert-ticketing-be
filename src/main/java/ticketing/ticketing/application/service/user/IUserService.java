package ticketing.ticketing.application.service.user;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface IUserService {
    public void saveOrUpdateUser(OAuth2User user);
}
