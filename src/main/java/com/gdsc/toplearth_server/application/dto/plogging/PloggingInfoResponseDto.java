package com.gdsc.toplearth_server.application.dto.plogging;

import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PloggingInfoResponseDto(
        Map<String, List<PloggingDetailResponseDto>> ploggingList
) {
    public static PloggingInfoResponseDto fromPloggingDetailResponseDtoList(
            Map<String, List<PloggingDetailResponseDto>> ploggingList) {
        return PloggingInfoResponseDto.builder()
                .ploggingList(ploggingList)
                .build();
    }
}
