package com.gdsc.toplearth_server.application.dto.bootstrap;

import lombok.Builder;

@Builder
public record HomeInfoResponseDto(
        Integer joinedTime,
        Integer ploggingMonthlyCount,
        Integer ploggingMonthlyDuration,
        Integer burnedCalories
) {
    public static HomeInfoResponseDto of(Integer joinedTime, Integer ploggingMonthlyCount,
                                         Integer ploggingMonthlyDuration, Integer burnedCalories) {
        return HomeInfoResponseDto.builder()
                .joinedTime(joinedTime)
                .ploggingMonthlyCount(ploggingMonthlyCount)
                .ploggingMonthlyDuration(ploggingMonthlyDuration)
                .burnedCalories(burnedCalories)
                .build();
    }
}
