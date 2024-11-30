package com.gdsc.toplearth_server.infrastructure.message;

import com.gdsc.toplearth_server.core.annotation.UserId;
import com.gdsc.toplearth_server.core.constant.Constants;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StompMessageController {
    private final StompMessageService stompMessageService;

    // 플로깅 정보 주고받기
    @MessageMapping("plogging.{matchingId}") // /pub/plogging.{matchingId}로 클라이언트에서 메시지 전송
    @SendTo("/sub/plogging.{matchingId}")    // /sub/plogging.{matchingId}로 클라이언트에 메시지 전달
    public PloggingInfoMessage sendPloggingInfo(
            @DestinationVariable Long matchingId,
            @Payload PloggingInfoMessage ploggingInfoMessage,
            @UserId UUID userId
    ) {
        log.info("Plogging info received for matchingId {}: {}", matchingId, ploggingInfoMessage);
        stompMessageService.processPloggingInfo(userId, ploggingInfoMessage);
        return ploggingInfoMessage; // 메시지를 브로드캐스트
    }

    // Only Debugging
    @RabbitListener(queues = Constants.MATCHING_QUEUE_NAME)
    public void receive(TeamInfoMessage teamInfoMessage) {
        log.info("received : {}", teamInfoMessage);
    }
}
