package com.gdsc.toplearth_server.application.dto.matching;

import com.gdsc.toplearth_server.application.dto.plogging.MatchingRecentPloggingResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record RecentMatchingInfo(
        List<MatchingRecentPloggingResponseDto> recentMatchingInfo
) {
    public static RecentMatchingInfo of(List<MatchingRecentPloggingResponseDto> recentMatchingInfo) {
        return RecentMatchingInfo.builder()
                .recentMatchingInfo(recentMatchingInfo)
                .build();
    }
}
