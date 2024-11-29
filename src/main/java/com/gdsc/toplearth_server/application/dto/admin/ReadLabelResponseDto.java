package com.gdsc.toplearth_server.application.dto.admin;

import com.gdsc.toplearth_server.application.dto.bootstrap.TrashInfoResponseDto;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record ReadLabelResponseDto(
        Map<String, List<AdminInfo>> monthlyInfo,
        TrashInfoResponseDto labelInfo
) {
    public static ReadLabelResponseDto of(Map<String, List<AdminInfo>> monthlyInfos,
                                          TrashInfoResponseDto labelInfo) {
        return ReadLabelResponseDto.builder()
                .monthlyInfo(monthlyInfos)
                .labelInfo(labelInfo)
                .build();
    }
}
