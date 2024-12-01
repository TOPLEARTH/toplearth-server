package com.gdsc.toplearth_server.application.dto.bootstrap;

import com.gdsc.toplearth_server.application.dto.matching.MatchingInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.mission.QuestInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.region.RegionRankInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.team.ReadTeamResponseDto;
import com.gdsc.toplearth_server.application.dto.user.UserInfoResponseDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record BootstrapResponseDto(
        UserInfoResponseDto userInfo,
        HomeInfoResponseDto homeInfo,
        MatchingInfoResponseDto matchingRealTimeInfo,
        QuestInfoResponseDto questInfo,
        ReadTeamResponseDto teamInfo,
        PloggingInfoResponseDto ploggingInfo,
        LegacyInfoResponseDto legacyInfo,
        List<RegionRankInfoResponseDto> regionRankingInfo
) {
    public static BootstrapResponseDto of(
            UserInfoResponseDto userInfo,
            HomeInfoResponseDto homInfo,
            MatchingInfoResponseDto matchingRealTimeInfo,
            QuestInfoResponseDto questInfo,
            ReadTeamResponseDto teamInfo,
            PloggingInfoResponseDto ploggingInfo,
            LegacyInfoResponseDto legacyInfo,
            List<RegionRankInfoResponseDto> regionRankingInfo
    ) {
        return BootstrapResponseDto.builder()
                .userInfo(userInfo)
                .homeInfo(homInfo)
                .matchingRealTimeInfo(matchingRealTimeInfo)
                .questInfo(questInfo)
                .teamInfo(teamInfo)
                .ploggingInfo(ploggingInfo)
                .legacyInfo(legacyInfo)
                .regionRankingInfo(regionRankingInfo)
                .build();
    }
}
