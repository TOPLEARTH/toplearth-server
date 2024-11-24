package com.gdsc.toplearth_server.application.dto.plogging;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PloggingInfoResponseDto(
        List<PloggingDetailResponseDto> ploggingList
) {
    public static PloggingInfoResponseDto fromPloggingDetailResponseDtoList(
            List<PloggingDetailResponseDto> ploggingList) {
        return PloggingInfoResponseDto.builder()
                .ploggingList(ploggingList)
                .build();
    }
}
