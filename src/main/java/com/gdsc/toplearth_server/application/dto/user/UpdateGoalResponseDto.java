package com.gdsc.toplearth_server.application.dto.user;

import lombok.Builder;

@Builder
public record UpdateGoalResponseDto(
        Double goalDistance
) {
}
