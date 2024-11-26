package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.application.dto.user.UpdateGoalResponseDto;
import com.gdsc.toplearth_server.application.service.user.UserService;
import com.gdsc.toplearth_server.core.annotation.UserId;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import com.gdsc.toplearth_server.presentation.request.user.UpdateGoalRequestDto;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserContoller {
    private final UserService userService;

    @PatchMapping("/goal")
    public CommonResponseDto<UpdateGoalResponseDto> updateGole(
            @Valid @RequestBody UpdateGoalRequestDto updateGoalRequestDto,
            @UserId UUID userId
    ) {
        return CommonResponseDto.ok(userService.updateGoal(updateGoalRequestDto, userId));
    }

}
