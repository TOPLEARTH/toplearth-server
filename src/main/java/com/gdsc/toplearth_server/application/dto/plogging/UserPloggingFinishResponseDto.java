package com.gdsc.toplearth_server.application.dto.plogging;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserPloggingFinishResponseDto(
        List<PloggingImageResponseDto> ploggingImages
) {
    public static UserPloggingFinishResponseDto fromEntityList(
            List<PloggingImageResponseDto> ploggingImageResponseDtoList
    ) {
        return UserPloggingFinishResponseDto.builder()
                .ploggingImages(ploggingImageResponseDtoList)
                .build();
    }
}
