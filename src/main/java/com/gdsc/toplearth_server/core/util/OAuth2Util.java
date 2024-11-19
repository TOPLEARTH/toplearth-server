package com.gdsc.toplearth_server.core.util;

import com.gdsc.toplearth_server.application.dto.oauth.ApplePublicKeyListDto;
import com.gdsc.toplearth_server.application.dto.oauth.OAuth2UserInfoResponseDto;
import com.gdsc.toplearth_server.application.service.oauth.AppleJwtParser;
import com.gdsc.toplearth_server.application.service.oauth.ApplePublicKeyGenerator;
import com.gdsc.toplearth_server.core.constant.Constants;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class OAuth2Util {
    private final RestClient restClient;

    public OAuth2Util(AppleJwtParser appleJwtParser, ApplePublicKeyGenerator applePublicKeyGenerator) {
        this.appleJwtParser = appleJwtParser;
        this.applePublicKeyGenerator = applePublicKeyGenerator;
        this.restClient = RestClient.builder().build();
    }

    public OAuth2UserInfoResponseDto getKakaoUserInfo(String kakaoAccessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX + kakaoAccessToken);
        httpHeaders.add(Constants.CONTENT_TYPE, Constants.APPLICATION_FORM_URLENCODED_WITH_CHARSET);

        try {
            String response = restClient.method(HttpMethod.POST)
                    .uri(Constants.KAKAO_USER_INFO_URI)
                    .headers(headers -> headers.addAll(httpHeaders))
                    .retrieve()
                    .body(String.class);

            if (response == null || response.isEmpty()) {
                throw new RuntimeException("Kakao 유저 정보 요청에 실패했습니다.");
            }

            JsonElement element = JsonParser.parseString(response);

            return OAuth2UserInfoResponseDto.of(
                    element.getAsJsonObject().get("id").getAsString(),
                    element.getAsJsonObject().getAsJsonObject("kakao_account").get("email").getAsString()
            );

        } catch (RestClientException e) {
            log.error("Kakao 유저 정보 요청 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("Kakao 유저 정보 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private final RestTemplate restTemplate = new RestTemplate();
    private final AppleJwtParser appleJwtParser;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;

    public OAuth2UserInfoResponseDto getAppleUserInfo(String appleIdToken) {
        Map<String, String> headers = appleJwtParser.parseHeaders(appleIdToken);
        ResponseEntity<ApplePublicKeyListDto> applePublicKeys = restTemplate.getForEntity(
                Constants.APPLE_PUBLIC_KEYS_URL,
                ApplePublicKeyListDto.class
        );
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers, applePublicKeys.getBody());
        Claims claims = appleJwtParser.parseClaims(appleIdToken, publicKey);
        return OAuth2UserInfoResponseDto.of(
                claims.get("sub", String.class),
                claims.get("email", String.class)
        );
    }
}

