package com.gdsc.toplearth_server.application.dto.matching;

import com.gdsc.toplearth_server.domain.entity.matching.type.EMatchingStatus;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MatchingStatusResponseDto(
        String status
) {
    public static MatchingStatusResponseDto of(EMatchingStatus status) {
        return MatchingStatusResponseDto.builder()
                .status(status.toString())
                .build();
    }
}
