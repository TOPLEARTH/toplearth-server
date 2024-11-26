package com.gdsc.toplearth_server.application.dto.plogging;

import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PloggingImageDetailResponseDto(
        String ploggingImageId, // 플로깅 이미지 ID
        String imageUrl, // 플로깅 이미지 URL
        String createdAt, // 플로깅 이미지 생성일
        Double latitude, // 플로깅 이미지 위도
        Double longitude, // 플로깅 이미지 경도
        String label // 플로깅 이미지 라벨
) {
    public static PloggingImageDetailResponseDto fromPloggingImageEntity(PloggingImage ploggingImage) {
        return PloggingImageDetailResponseDto.builder()
                .ploggingImageId(ploggingImage.getId() == null ? null : ploggingImage.getId().toString())
                .imageUrl(ploggingImage.getImage())
                .createdAt(ploggingImage.getCreatedAt() == null ? null : ploggingImage.getCreatedAt().toString())
                .latitude(ploggingImage.getLatitude())
                .longitude(ploggingImage.getLongitude())
                .label(ploggingImage.getELabel() == null ? null : ploggingImage.getELabel().toString())
                .build();
    }
}
