package com.gdsc.toplearth_server.presentation.request.matching;

public record VSFinishRequestDto(
        Long competitionScore,
        Integer totalPickUpCnt,
        Boolean winFlag
) {
}
