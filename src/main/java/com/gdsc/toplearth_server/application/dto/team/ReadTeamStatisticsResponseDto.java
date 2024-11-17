package com.gdsc.toplearth_server.application.dto.team;

import java.util.List;

public record ReadTeamStatisticsResponseDto(
        List<ReadTeamDistanceResponseDto> distances,
        List<ReadTeamLabelResponseDto> labels
) {
}
