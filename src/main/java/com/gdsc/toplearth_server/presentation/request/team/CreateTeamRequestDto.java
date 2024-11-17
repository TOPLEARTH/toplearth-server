package com.gdsc.toplearth_server.presentation.request.team;

import jakarta.validation.constraints.NotNull;

public record CreateTeamRequestDto(
        @NotNull(message = "팀 이름을 입력하세요.")
        String name
) {
}
