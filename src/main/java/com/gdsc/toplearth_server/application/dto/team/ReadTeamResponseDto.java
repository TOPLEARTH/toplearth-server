package com.gdsc.toplearth_server.application.dto.team;

import com.gdsc.toplearth_server.domain.entity.team.Teams;
import java.time.YearMonth;
import java.util.Map;
import lombok.Builder;

@Builder
public record ReadTeamResponseDto(
        Long teamId,
        String teamCode,
        String teamName,
        Integer matchCnt,
        Integer winCnt,
        Map<YearMonth, ReadTeamStatisticsResponseDto> teamSelect
) {
    //of 메소드
    public static ReadTeamResponseDto of(Teams teams, Integer matchCnt, Integer winCnt,
                                         Map<YearMonth, ReadTeamStatisticsResponseDto> readTeamSelectResponseDtos) {
        return ReadTeamResponseDto.builder()
                .teamId(teams.getId())
                .teamCode(teams.getCode())
                .teamName(teams.getName())
                .matchCnt(matchCnt)
                .winCnt(winCnt)
                .teamSelect(readTeamSelectResponseDtos)
                .build();
    }
}
