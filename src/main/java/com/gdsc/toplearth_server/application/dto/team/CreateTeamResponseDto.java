package com.gdsc.toplearth_server.application.dto.team;

import lombok.Builder;

@Builder
public record CreateTeamResponseDto(
        String name,
        String code
) {
}
