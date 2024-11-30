package com.gdsc.toplearth_server.application.dto.matching;

import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@Builder
public record MatchingInfoResponseDto(
        String matchingStartedAt,
        String ourTeamName,
        String opponentTeamName
) {
    public static MatchingInfoResponseDto of(Matching matching) {
        return MatchingInfoResponseDto.builder()
                .matchingStartedAt(matching.getStartedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .ourTeamName(matching.getTeam().getName())
                .opponentTeamName(matching.getOpponentTeam().getName())
                .build();
    }
}
