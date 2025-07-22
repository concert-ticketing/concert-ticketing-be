package ticketing.ticketing.application.service.oauth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.service.user.UserService;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.domain.enums.UserState;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;
import ticketing.ticketing.infrastructure.security.CustomOAuth2User;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String,Object> attributes = oauth2User.getAttributes();

        User createUser;

        if (registrationId.equals("google")) {
            createUser = User.create((String) attributes.get("sub"), UserState.ACTIVE);
        } else if (registrationId.equals("naver")) {
            Map<String, Object> naverAttributes = (Map<String, Object>) attributes.get("response");
            createUser = User.create(String.valueOf(naverAttributes.get("id")), UserState.ACTIVE);
        } else {
            createUser = User.create(String.valueOf(attributes.get("id")), UserState.ACTIVE);
        }

        userRepository.save(createUser);

        return new CustomOAuth2User(oauth2User, createUser); // 🔥 여기서 래핑해서 넘김
    }
}
