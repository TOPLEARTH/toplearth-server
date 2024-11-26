package com.gdsc.toplearth_server.application.dto.plogging;

import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserPloggingStartResponseDto(
        Long ploggingId    // 플로깅 ID
) {
    public static UserPloggingStartResponseDto fromEntity(Plogging plogging) {
        return UserPloggingStartResponseDto.builder()
                .ploggingId(plogging.getId())
                .build();
    }
}
