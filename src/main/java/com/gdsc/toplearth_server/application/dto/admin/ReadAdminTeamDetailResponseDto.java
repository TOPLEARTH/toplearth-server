package com.gdsc.toplearth_server.application.dto.admin;

import com.gdsc.toplearth_server.domain.entity.team.Team;
import java.util.List;
import lombok.Builder;

@Builder
public record ReadAdminTeamDetailResponseDto(
        Long teamId,
        String teamName,
        String teamCode,
        Integer matchCnt,
        Integer winCnt,
        List<ReadTeamMemberResponseDto> memberList
) {
    public static ReadAdminTeamDetailResponseDto of(Team team, Integer matchCnt, Integer winCnt,
                                                    List<ReadTeamMemberResponseDto> readTeamMemberResonseDto) {
        return ReadAdminTeamDetailResponseDto.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .teamCode(team.getCode())
                .matchCnt(matchCnt)
                .winCnt(winCnt)
                .memberList(readTeamMemberResonseDto)
                .build();
    }
}
