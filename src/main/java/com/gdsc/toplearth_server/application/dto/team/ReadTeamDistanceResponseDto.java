package com.gdsc.toplearth_server.application.dto.team;

import com.gdsc.toplearth_server.domain.entity.team.Member;
import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
import lombok.Builder;

@Builder
public record ReadTeamDistanceResponseDto(
        Long id,
        String name,
        ETeamRole role,
        Double distance
) {
    public static ReadTeamDistanceResponseDto of(Member member, Double distance) {
        return ReadTeamDistanceResponseDto.builder()
                .id(member.getId())
                .name(member.getUser().getNickname())
                .role(member.getTeamRole())
                .distance(distance)
                .build();
    }
}
