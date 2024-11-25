package com.gdsc.toplearth_server.application.dto.plogging;

import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PloggingDetailResponseDto(
        Long ploggingId, // 플로깅 ID
        Double distance,    // 플로깅 총 이동거리
        Long duration,    // 플로깅 소요 시간
        Integer trashCnt,  // 플로깅 총 쓰레기 개수
        String startedAt,    // 플로깅 시작 시간
        String endedAt,   // 플로깅 종료 시간
        PloggingTeamInfoResponseDto ploggingTeamInfo,   // 플로깅 팀 정보
        List<PloggingImageDetailResponseDto> ploggingImageList  // 플로깅 이미지 리스트
) {
    public static PloggingDetailResponseDto fromPloggingEntity(
            Plogging plogging,
            List<PloggingImage> ploggingImageList,
            PloggingTeamInfoResponseDto ploggingTeamInfo
    ) {
        return PloggingDetailResponseDto.builder()
                .ploggingId(plogging.getId())
                .distance(plogging.getDistance().doubleValue())
                .duration(plogging.getDuration())
                .trashCnt(plogging.getPickUpCnt())
                .startedAt(plogging.getStartedAt() == null ? null : plogging.getStartedAt().toString())
                .endedAt(plogging.getEndedAt() == null ? null : plogging.getEndedAt().toString())
                .ploggingTeamInfo(ploggingTeamInfo)
                .ploggingImageList(ploggingImageList.stream()
                        .map(PloggingImageDetailResponseDto::fromPloggingImageEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
