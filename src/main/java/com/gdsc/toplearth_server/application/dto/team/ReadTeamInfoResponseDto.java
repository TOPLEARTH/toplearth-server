package com.gdsc.toplearth_server.application.dto.team;

import lombok.Builder;

@Builder
public record ReadTeamInfoResponseDto(
        Long teamId,
        String name,
        String code

) {
}
