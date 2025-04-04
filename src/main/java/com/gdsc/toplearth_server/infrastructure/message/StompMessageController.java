package com.gdsc.toplearth_server.infrastructure.message;

import com.gdsc.toplearth_server.core.annotation.UserId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StompMessageController {
    private final StompMessageService stompMessageService;

    // 플로깅 정보 주고받기
    // @SendTo("/sub/{matchingId}.plogging")    // /sub/{matchingId}.plogging로로 클라이언트에 메시지 전달: but, sendTo 생략 가능 => default
    @MessageMapping("{matchingId}.plogging") // /pub/{matchingId}.plogging로 클라이언트에서 메시지 전송
    public PloggingInfoMessage sendPloggingInfo(
            @DestinationVariable Long matchingId,
            @Payload PloggingInfoMessage ploggingInfoMessage,
            @UserId UUID userId
    ) {
        log.info("Plogging info received for matchingId {}: {}", matchingId, ploggingInfoMessage.ploggingId());
        stompMessageService.processPloggingInfo(userId, matchingId, ploggingInfoMessage);
        return ploggingInfoMessage; // 메시지를 브로드캐스트
    }

    // Only Debugging
    // 리스너 만드는 순간 큐에 삽입 시 메시지가 바로 소비됨. -> 이 앱에서는 악질인 놈
//    @RabbitListener(queues = Constants.VS_QUEUE_NAME)
//    public void receive(TeamInfoMessage teamInfoMessage) {
//        log.info("received : {}", teamInfoMessage);
//    }
}
