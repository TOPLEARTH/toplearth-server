//package com.gdsc.toplearth_server.core.scheduler;
//
//import com.gdsc.toplearth_server.application.service.matching.MatchingService;
//import com.gdsc.toplearth_server.domain.entity.matching.Matching;
//import com.gdsc.toplearth_server.infrastructure.repository.matching.MatchingRepositoryImpl;
//import java.time.LocalDateTime;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@RequiredArgsConstructor
//@Component
//@Slf4j
//public class FinishMatchingScheduler {
//    private final MatchingService matchingService;
//    private final MatchingRepositoryImpl matchingRepositoryImpl;
//    /**
//     * 매 정각마다 실행되는 매칭 스케줄러
//     * cron 표현식: "0 0 * * * *" -> 매 정각(매 시 0분 0초)
//     */
//    // @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(cron = "0 */3 * * * *")
//    @Transactional
//    public void scheduleFinishMatching() {
//        log.info("Scheduled finish matching process started at {}", LocalDateTime.now());
//
//        // 현재 시간에서 90분 전 시간 계산
//        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(90);
//
//        // 90분이 지난 매칭 조회
//        List<Matching> matchingsToFinish = matchingRepositoryImpl.findOngoingMatchingsOlderThan(thresholdTime);
//        log.info("Found {} matchings to finish.", matchingsToFinish.size());
//
//        // 매칭 종료 처리
//        matchingsToFinish.forEach(matchingService::finishVS);
//        log.info("Finished processing matchings at {}", LocalDateTime.now());
//
//    }
//}
