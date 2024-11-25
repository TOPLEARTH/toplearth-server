package com.gdsc.toplearth_server.application.dto.region;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public record RegionRankInfoResponseDto(
        Long regionId,
        String regionName,
        Long score,
        Integer ranking
) {
    public static RegionRankInfoResponseDto of(Long regionId, String regionName, Long score, Integer ranking) {
        return RegionRankInfoResponseDto.builder()
                .regionId(regionId)
                .regionName(regionName)
                .score(score)
                .ranking(ranking)
                .build();
    }
}
