package com.gdsc.toplearth_server.application.dto.bootstrap;

import lombok.Builder;

@Builder
public record HomeInfoResponseDto(
        Integer recentPloggingDay,
        Integer joinedTime,
        Integer ploggingMonthlyCount,
        Integer ploggingMonthlyDuration,
        Integer burnedCalories
) {
    public static HomeInfoResponseDto of(Integer recentPloggingDay, Integer joinedTime, Integer ploggingMonthlyCount,
                                         Integer ploggingMonthlyDuration, Integer burnedCalories) {
        return HomeInfoResponseDto.builder()
                .recentPloggingDay(recentPloggingDay)
                .joinedTime(joinedTime)
                .ploggingMonthlyCount(ploggingMonthlyCount)
                .ploggingMonthlyDuration(ploggingMonthlyDuration)
                .burnedCalories(burnedCalories)
                .build();
    }
}
