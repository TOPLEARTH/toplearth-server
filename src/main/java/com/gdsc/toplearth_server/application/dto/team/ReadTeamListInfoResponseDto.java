package com.gdsc.toplearth_server.application.dto.team;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ReadTeamListInfoResponseDto(
        List<ReadTeamInfoResponseDto> teams
) {
    public static ReadTeamListInfoResponseDto fromTeamDtoList(List<ReadTeamInfoResponseDto> teams) {
        return ReadTeamListInfoResponseDto.builder()
                .teams(teams)
                .build();
    }
}
