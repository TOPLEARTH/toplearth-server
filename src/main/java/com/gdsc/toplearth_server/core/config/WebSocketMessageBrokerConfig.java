package com.gdsc.toplearth_server.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Configuration
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOrigins("*"); // 개발 전용
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher(".")); // URL / -> .
        registry.setApplicationDestinationPrefixes("/pub");  //  @MessageMapping 메서드로 라우팅 Client에서 SEND 요청 처리
        registry.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
                .setRelayHost("localhost") // RabbitMQ 호스트
                .setRelayPort(61613)       // RabbitMQ의 STOMP 포트
                .setClientLogin("toplearth") // RabbitMQ 사용자
                .setClientPasscode("qwer1234!@"); // RabbitMQ 비밀번호
    }
}
