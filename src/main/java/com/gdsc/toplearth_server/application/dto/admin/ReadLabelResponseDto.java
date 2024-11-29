package com.gdsc.toplearth_server.application.dto.admin;

import com.gdsc.toplearth_server.application.dto.bootstrap.TrashInfoResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ReadLabelResponseDto(
        List<AdminInfo> monthlyInfo,
        TrashInfoResponseDto labelInfo
) {
    public static ReadLabelResponseDto of(List<AdminInfo> monthlyInfos,
                                          TrashInfoResponseDto labelInfo) {
        return ReadLabelResponseDto.builder()
                .monthlyInfo(monthlyInfos)
                .labelInfo(labelInfo)
                .build();
    }
}
