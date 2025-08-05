package ticketing.ticketing.application.service.user.oauth.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ticketing.ticketing.application.dto.userDto.UserOAuthTokenReadResponse;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetUserInfoFromNaverService {
    public UserOAuthTokenReadResponse getUserInfoFromNaver(String accessToken) {
        String url = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);  // Authorization: Bearer {accessToken}
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
            throw new RuntimeException("네이버 사용자 정보 조회 실패");
        }

        Map body = response.getBody();
        assert body != null;

        // 네이버는 response 내 response 객체에 사용자 정보 있음
        Map responseData = (Map) body.get("response");
        String id = String.valueOf(responseData.get("id"));

        return new UserOAuthTokenReadResponse(id);
    }
}
