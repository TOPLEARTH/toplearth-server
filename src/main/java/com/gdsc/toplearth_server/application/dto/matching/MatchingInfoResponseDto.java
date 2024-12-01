package com.gdsc.toplearth_server.application.dto.matching;

import lombok.Builder;

@Builder
public record MatchingInfoResponseDto(
        String matchingStartedAt,
        String ourTeamName,
        String opponentTeamName
) {
    public static MatchingInfoResponseDto of(String matchingStartedAt, String ourTeamName, String opponentTeamName) {
        return MatchingInfoResponseDto.builder()
                .matchingStartedAt(matchingStartedAt)
                .ourTeamName(ourTeamName)
                .opponentTeamName(opponentTeamName)
                .build();
    }
}
