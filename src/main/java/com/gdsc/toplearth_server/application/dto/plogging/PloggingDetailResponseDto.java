package com.gdsc.toplearth_server.application.dto.plogging;

import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PloggingDetailResponseDto(
        String ploggingId, // 플로깅 ID
        String distance,    // 플로깅 총 이동거리
        String duration,    // 플로깅 소요 시간
        String trashCnt,  // 플로깅 총 쓰레기 개수
        String startedAt,    // 플로깅 시작 시간
        String endedAt,   // 플로깅 종료 시간
        List<PloggingImageDetailResponseDto> ploggingImageList  // 플로깅 이미지 리스트
) {
    public static PloggingDetailResponseDto fromPloggingEntity(Plogging plogging, List<PloggingImage> ploggingImageList) {
        return PloggingDetailResponseDto.builder()
                .ploggingId(plogging.getId().toString())
                .distance(plogging.getDistance().toString())
                .duration(plogging.getDuration().toString())
                .trashCnt(plogging.getPickUpCnt().toString())
                .startedAt(plogging.getStartedAt().toString())
                .endedAt(plogging.getEndedAt().toString())
                .ploggingImageList(ploggingImageList.stream()
                        .map(PloggingImageDetailResponseDto::fromPloggingImageEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
