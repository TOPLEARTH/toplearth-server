package com.gdsc.toplearth_server.presentation.request.user;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record UpdateGoalRequestDto(
        @NotNull(message = "목표 거리를 입력해야합니다.")
        BigDecimal goalDistance

) {
}
