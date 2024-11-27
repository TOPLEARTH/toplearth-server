package com.gdsc.toplearth_server.application.dto.plogging;

import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PloggingImageResponseDto(
        Long ploggingImageId,
        String imageUrl,
        Double longitude,
        Double latitude,
        String createdAt
) {
    public static PloggingImageResponseDto fromPloggingImageEntity(PloggingImage ploggingImage) {
        return PloggingImageResponseDto.builder()
                .ploggingImageId(ploggingImage.getId())
                .imageUrl(ploggingImage.getImage())
                .longitude(ploggingImage.getLongitude())
                .latitude(ploggingImage.getLatitude())
                .createdAt(ploggingImage.getCreatedAt().toString())
                .build();
    }
}
