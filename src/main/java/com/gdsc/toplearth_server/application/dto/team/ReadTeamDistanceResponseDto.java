package com.gdsc.toplearth_server.application.dto.team;

import com.gdsc.toplearth_server.domain.entity.team.Members;
import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
import lombok.Builder;

@Builder
public record ReadTeamDistanceResponseDto(
        Long id,
        String name,
        ETeamRole role,
        Double distance
) {
    public static ReadTeamDistanceResponseDto of(Members members, Double distance) {
        return ReadTeamDistanceResponseDto.builder()
                .id(members.getId())
                .name(members.getUser().getNickname())
                .role(members.getETeamRole())
                .distance(distance)
                .build();
    }
}
