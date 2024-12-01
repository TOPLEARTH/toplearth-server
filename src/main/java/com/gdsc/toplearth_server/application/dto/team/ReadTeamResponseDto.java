package com.gdsc.toplearth_server.application.dto.team;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record ReadTeamResponseDto(
        Long teamId,
        String teamCode,
        String teamName,
        Integer matchCnt,
        Integer winCnt,
        List<TeamMemberResponseDto> teamMemebers,
        Map<YearMonth, ReadTeamStatisticsResponseDto> monthlyData
) {
    //of 메소드
    public static ReadTeamResponseDto of(Long teamId, String teamCode, String teamName, Integer matchCnt,
                                         Integer winCnt,
                                         List<TeamMemberResponseDto> teamMembers,
                                         Map<YearMonth, ReadTeamStatisticsResponseDto> readTeamSelectResponseDtos) {
        return ReadTeamResponseDto.builder()
                .teamId(teamId)
                .teamCode(teamCode)
                .teamName(teamName)
                .matchCnt(matchCnt)
                .winCnt(winCnt)
                .teamMemebers(teamMembers)
                .monthlyData(readTeamSelectResponseDtos)
                .build();
    }
}
