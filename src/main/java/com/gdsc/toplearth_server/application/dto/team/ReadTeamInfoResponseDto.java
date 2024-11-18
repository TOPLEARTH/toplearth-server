package com.gdsc.toplearth_server.application.dto.team;

import com.gdsc.toplearth_server.domain.entity.team.Team;
import lombok.Builder;

@Builder
public record ReadTeamInfoResponseDto(
        Long teamId,
        String teamName,
        String teamCode

) {
    public static ReadTeamInfoResponseDto of(Team team) {
        return ReadTeamInfoResponseDto.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .teamCode(team.getCode())
                .build();
    }
}
