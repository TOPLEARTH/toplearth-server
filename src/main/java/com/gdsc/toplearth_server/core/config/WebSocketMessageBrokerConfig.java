package com.gdsc.toplearth_server.core.config;

import com.gdsc.toplearth_server.core.interceptor.pre.JwtWebSocketChannelInterceptor;
import com.gdsc.toplearth_server.core.interceptor.pre.JwtWebSocketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Configuration
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtWebSocketChannelInterceptor jwtWebSocketChannelInterceptor;
    private final JwtWebSocketHandshakeInterceptor jwtHandshakeInterceptor;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private Integer stompPort;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOrigins("*"); // 개발 전용
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher(".")); // URL / -> .
        registry.setApplicationDestinationPrefixes("/pub");  //  @MessageMapping 메서드로 라우팅 Client에서 SEND 요청 처리
        registry.enableStompBrokerRelay("/sub") // @SendTo 메서드로 라우팅
                .setRelayHost(host) // RabbitMQ 호스트
                .setRelayPort(stompPort)       // RabbitMQ의 STOMP 포트
                .setSystemLogin(username) // RabbitMQ 시스템 사용자
                .setSystemPasscode(password) // RabbitMQ 시스템 비밀번호 -> 없으면 권한 에러
                .setClientLogin(username)
                .setClientPasscode(password);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtWebSocketChannelInterceptor);
    }
}
