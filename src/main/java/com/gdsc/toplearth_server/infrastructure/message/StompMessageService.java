package com.gdsc.toplearth_server.infrastructure.message;

import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.infrastructure.repository.matching.MatchingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StompMessageService {
    private final UserRepositoryImpl userRepositoryImpl;
    private final PloggingRepositoryImpl ploggingRepository;
    private final MatchingRepositoryImpl matchingRepositoryImpl;

    // STOMP 메시지를 처리
    @Transactional
    public void processPloggingInfo(UUID userId, Long matchingId, PloggingInfoMessage ploggingInfoMessage) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Matching matching = matchingRepositoryImpl.findById(matchingId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MATCH));

        Plogging plogging = ploggingRepository.findById(ploggingInfoMessage.ploggingId())
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PLOGGING));

        plogging.updateRealTimePloggingInfo(ploggingInfoMessage.distance(), ploggingInfoMessage.trashCnt());
        ploggingRepository.save(plogging);

        log.info("Plogging info processed: {}", ploggingInfoMessage);
    }
}
