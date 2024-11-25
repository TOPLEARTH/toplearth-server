package com.gdsc.toplearth_server.application.dto.region;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public record RegionRankInfoResponseDto(
        String regionId,
        String regionName,
        String score,
        String ranking
) {
    public static RegionRankInfoResponseDto of(String regionId, String regionName, String score, String ranking) {
        return RegionRankInfoResponseDto.builder()
                .regionId(regionId)
                .regionName(regionName)
                .score(score)
                .ranking(ranking)
                .build();
    }
}
