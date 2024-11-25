package com.gdsc.toplearth_server.infrastructure.event;

import com.gdsc.toplearth_server.application.service.matching.MatchingService;
import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.presentation.request.matching.MatchingRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingConsumer {
    private final MatchingService matchingService;

    @RabbitListener(queues = Constants.MATCHING_QUEUE_NAME)
    public void consumeMatchingRequests(List<MatchingRequestDto> messages) {
        log.info("Received {} matching requests from RabbitMQ.", messages.size());
        matchingService.processMatchingRequests(messages);
    }
}
