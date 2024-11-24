package com.gdsc.toplearth_server.application.dto.plogging;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PloggingTeamInfoResponseDto(
        String teamId, // 팀 ID
        String teamName, // 팀 이름
        String opponentTeamId, // 상대 팀 ID
        String opponentTeamName // 상대 팀 이름
) {
    public static PloggingTeamInfoResponseDto fromPloggingTeamEntity(String teamId, String teamName,
                                                                     String opponentTeamId, String opponentTeamName) {
        return PloggingTeamInfoResponseDto.builder()
                .teamId(teamId)
                .teamName(teamName)
                .opponentTeamId(opponentTeamId)
                .opponentTeamName(opponentTeamName)
                .build();
    }

    public static PloggingTeamInfoResponseDto ofNull() {
        return PloggingTeamInfoResponseDto.builder()
                .teamId(null)
                .teamName(null)
                .opponentTeamId(null)
                .opponentTeamName(null)
                .build();
    }
}
