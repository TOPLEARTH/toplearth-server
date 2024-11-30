package com.gdsc.toplearth_server.core.interceptor.pre;

import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.core.util.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
// WebSocket handshake 시 JWT 검증을 위한 인터셉터
public class JwtWebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        log.info(String.valueOf(request.getURI()));
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(Constants.BEARER_PREFIX)) {
            String token = authHeader.substring(Constants.BEARER_PREFIX.length());

            Claims claims = jwtUtil.validateToken(token);
            UUID userId = UUID.fromString(claims.get(Constants.USER_ID_CLAIM_NAME, String.class));

            attributes.put(Constants.USER_ID_CLAIM_NAME, userId);
            log.info("userId : {}", userId);

            return true;
        } else {
            throw new CustomException(ErrorCode.EMPTY_AUTHENTICATION);
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        log.info("WebSocket handshake completed for {}", request.getRemoteAddress());
    }
}
