package com.gdsc.toplearth_server.core.interceptor.pre;

import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.core.util.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
// STOMP 메시지에서 검증된 JWT를 추출하여 인증 객체를 생성하는 인터셉터
public class JwtWebSocketChannelInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // WebSocket 연결 요청 시 (STOMP CONNECT)
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String bearerToken = accessor.getFirstNativeHeader(Constants.AUTHORIZATION_HEADER);
            log.info("bearerToken : {}", bearerToken);
            if (bearerToken != null && bearerToken.startsWith(Constants.BEARER_PREFIX)) {
                String token = bearerToken.substring(Constants.BEARER_PREFIX.length());
                Claims claims = jwtUtil.validateToken(token);
                UUID userId = UUID.fromString(claims.get(Constants.USER_ID_CLAIM_NAME, String.class));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, null);

                accessor.setUser(authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new IllegalArgumentException("No JWT token found in request headers");
            }
        }

        return message;
    }
}
