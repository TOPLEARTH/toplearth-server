package com.gdsc.toplearth_server.application.dto.user;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record UpdateGoalResponseDto(
        BigDecimal goalDistance
) {
}
