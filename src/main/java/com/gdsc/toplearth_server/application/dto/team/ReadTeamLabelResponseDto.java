package com.gdsc.toplearth_server.application.dto.team;

import com.gdsc.toplearth_server.domain.entity.plogging.type.ELabel;

public record ReadTeamLabelResponseDto(
        ELabel label,
        Integer count
) {
}
