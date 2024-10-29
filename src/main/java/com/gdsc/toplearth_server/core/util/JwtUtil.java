package com.gdsc.toplearth_server.core.util;

import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.core.security.JwtDto;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil implements InitializingBean {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.accessExpiredMs}")
    private Long accessExpireMs;

    @Value("${jwt.refreshExpiredMs}")
    private Long refreshExpireMs;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public JwtDto generateTokens(UUID id, EUserRole role) {
        return new JwtDto(
                generateToken(id, role, accessExpireMs),
                generateToken(id, null, refreshExpireMs)
        );
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String generateToken(UUID id, EUserRole role, Long expirePeriod) {
        Claims claims = Jwts.claims();
        claims.put(Constants.USER_ID_CLAIM_NAME, id.toString());
        if (role != null)
            claims.put(Constants.USER_ROLE_CLAIM_NAME, role);

        return Jwts.builder()
                .setHeaderParam(Header.JWT_TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirePeriod))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }




}
