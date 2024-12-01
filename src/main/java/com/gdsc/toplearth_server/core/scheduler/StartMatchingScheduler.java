package com.gdsc.toplearth_server.core.scheduler;

import com.gdsc.toplearth_server.application.service.matching.MatchingService;
import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.infrastructure.message.TeamInfoMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class StartMatchingScheduler {    // 정각마다 매칭 진행
    private final MatchingService matchingService;
    private final RabbitTemplate rabbitTemplate;

    /**
     * 매 정각마다 실행되는 매칭 스케줄러
     * cron 표현식: "0 0 * * * *" -> 매 정각(매 시 0분 0초)
     */
    // @Scheduled(cron = "0 0 * * * *")
    @Scheduled(cron = "0 */3 * * * *")
    @Transactional
    public void scheduleStartMatching() {
        log.info("Scheduled matching process started at {}", LocalDateTime.now());

        try {
            // RabbitMQ 큐에서 모든 매칭 요청 가져오기
            List<TeamInfoMessage> requests = new ArrayList<>();
            log.info("Fetching matching requests from RabbitMQ...");
            Object message;

            while ((message = rabbitTemplate.receiveAndConvert(Constants.MATCHING_QUEUE_NAME)) != null) {
                if (message instanceof TeamInfoMessage teamInfoMessage) {
                    requests.add(teamInfoMessage);
                    log.info("Fetched matching request from RabbitMQ: {}", teamInfoMessage.teamId());
                } else {
                    log.warn("Invalid message type in queue: {}", message);
                }
            }

            // 매칭 요청이 있을 경우 처리
            if (!requests.isEmpty()) {
                log.info("Fetched {} matching requests from RabbitMQ.", requests.size());
                matchingService.processRandomMatchingRequests(requests); // 매칭 처리
                log.info("Processed {} matching requests successfully.", requests.size());
            } else {
                log.info("No matching requests found in RabbitMQ.");
            }
        } catch (Exception e) {
            log.error("Error during matching process: {}", e.getMessage(), e);
        }
    }
}
