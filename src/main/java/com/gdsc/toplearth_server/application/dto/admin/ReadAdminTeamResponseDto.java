package com.gdsc.toplearth_server.application.dto.admin;

import com.gdsc.toplearth_server.domain.entity.team.Team;
import lombok.Builder;

@Builder
public record ReadAdminTeamResponseDto(
        Long teamId,
        String name,
        String code
) {
    public static ReadAdminTeamResponseDto of(Team team) {
        return ReadAdminTeamResponseDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .code(team.getCode())
                .build();
    }
}
