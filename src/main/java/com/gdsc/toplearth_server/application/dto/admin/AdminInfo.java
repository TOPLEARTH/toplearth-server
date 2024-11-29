package com.gdsc.toplearth_server.application.dto.admin;

import lombok.Builder;

@Builder
public record AdminInfo(
        Long monthlyTrashCnt,
        Long monthlyPloggingTime
) {
    public static AdminInfo of(Long monthlyTrashCnt, Long monthlyPloggingTime) {
        return AdminInfo.builder()
                .monthlyTrashCnt(monthlyTrashCnt)
                .monthlyPloggingTime(monthlyPloggingTime)
                .build();

    }
}
