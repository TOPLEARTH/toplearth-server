package com.gdsc.toplearth_server.presentation.request.user;

import jakarta.validation.constraints.NotNull;

public record UpdateGoalRequestDto(
        @NotNull(message = "목표 거리를 입력해야합니다.")
        Double goalDistance

) {
}
