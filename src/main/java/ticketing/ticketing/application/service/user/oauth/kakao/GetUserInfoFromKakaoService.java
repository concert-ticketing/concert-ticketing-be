package ticketing.ticketing.application.service.user.oauth.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ticketing.ticketing.application.dto.userDto.UserOAuthTokenReadResponse;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetUserInfoFromKakaoService {

    public UserOAuthTokenReadResponse getUserInfoFromKakao(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Authorization: Bearer {accessToken}
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패");
        }

        Map body = response.getBody();
        assert body != null;
        String id = String.valueOf(body.get("id"));

        return new UserOAuthTokenReadResponse(id);
    }
}
