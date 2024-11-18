package com.gdsc.toplearth_server.application.dto.team;

import com.gdsc.toplearth_server.domain.entity.team.Team;
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
    public static ReadTeamResponseDto of(Team team, Integer matchCnt, Integer winCnt,
                                         Map<YearMonth, ReadTeamStatisticsResponseDto> readTeamSelectResponseDtos) {
        return ReadTeamResponseDto.builder()
                .teamId(team.getId())
                .teamCode(team.getCode())
                .teamName(team.getName())
                .matchCnt(matchCnt)
                .winCnt(winCnt)
                .teamSelect(readTeamSelectResponseDtos)
                .build();
    }
}
