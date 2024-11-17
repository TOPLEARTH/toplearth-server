package com.gdsc.toplearth_server.domain.entity.team.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ETeamRole {
    LEADER("LEADER"),
    MEMBER("MEMBER");

    private final String teamRole;
}
