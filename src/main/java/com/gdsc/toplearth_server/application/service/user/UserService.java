package com.gdsc.toplearth_server.application.service.user;

import com.gdsc.toplearth_server.application.dto.user.UpdateGoalResponseDto;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.user.Users;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import com.gdsc.toplearth_server.presentation.request.user.UpdateGoalRequestDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepositoryImpl userRepository;

    public UpdateGoalResponseDto updateGoal(UpdateGoalRequestDto updateGoalRequestDto, UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        user.updateGoal(updateGoalRequestDto.goalDistance());

        return UpdateGoalResponseDto.builder()
                .goalDistance(user.getGoalDistance())
                .build();
    }
}
