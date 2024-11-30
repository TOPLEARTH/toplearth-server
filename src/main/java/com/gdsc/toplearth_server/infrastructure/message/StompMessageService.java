package com.gdsc.toplearth_server.infrastructure.message;

import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StompMessageService {
    private final UserRepositoryImpl userRepositoryImpl;
    // STOMP 메시지를 처리하는 서비스
    public void processPloggingInfo(UUID userId, PloggingInfoMessage ploggingInfoMessage) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        log.info("Plogging info processed: {}", ploggingInfoMessage);
    }
}
