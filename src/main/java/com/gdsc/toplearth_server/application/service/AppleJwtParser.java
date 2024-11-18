package com.gdsc.toplearth_server.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AppleJwtParser {
    private static final String TOKEN_VALUE_DELIMITER = "\\.";
    private static final int HEADER_INDEX = 0;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Apple IdToken header에 ALG와 KID 추출
    public Map<String, String> parseHeaders(String idToken) {
        try {
            String encodedHeader = idToken.split(TOKEN_VALUE_DELIMITER)[HEADER_INDEX];
            String decodedHeader = new String(Base64.getUrlDecoder().decode(encodedHeader));
            return OBJECT_MAPPER.readValue(decodedHeader, Map.class);
        } catch (JsonProcessingException | ArrayIndexOutOfBoundsException e) {
            throw new CustomException(ErrorCode.INVALID_APPLE_IDENTITY_TOKEN_ERROR);
        }
    }

    // PublicKey로 Identity Token Claim 추출
    public Claims parseClaims(String idToken, PublicKey publicKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_APPLE_IDENTITY_TOKEN_ERROR);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_APPLE_IDENTITY_TOKEN_ERROR);
        }
    }
}
