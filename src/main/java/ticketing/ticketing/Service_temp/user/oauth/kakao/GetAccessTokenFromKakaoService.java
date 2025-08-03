package ticketing.ticketing.application.service.user.oauth.kakao;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class GetAccessTokenFromKakaoService {

    public String getAccessTokenFromKakao(String code) {
        Dotenv dotenv = Dotenv.load();
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", dotenv.get("KAKAO_CLIENT_ID"));
        body.add("redirect_uri", "http://localhost:3000/oauth/success"); // 클라이언트에서 보낸 redirect_uri
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        } else {
            throw new RuntimeException("카카오 access token 요청 실패");
        }
    }
}
