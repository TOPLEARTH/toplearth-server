package com.gdsc.toplearth_server.application.dto.bootstrap;

import com.gdsc.toplearth_server.application.dto.mission.QuestInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.user.UserInfoResponseDto;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record BootstrapResponseDto(
        UserInfoResponseDto userInfo,
        QuestInfoResponseDto questInfo,
        PloggingInfoResponseDto ploggingInfo
) {
    public static BootstrapResponseDto of(
            UserInfoResponseDto userInfo,
            QuestInfoResponseDto questInfo,
            PloggingInfoResponseDto ploggingInfo
    ) {
        return BootstrapResponseDto.builder()
                .userInfo(userInfo)
                .questInfo(questInfo)
                .ploggingInfo(ploggingInfo)
                .build();
    }
}
