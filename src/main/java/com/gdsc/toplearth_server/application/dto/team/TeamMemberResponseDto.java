package com.gdsc.toplearth_server.application.dto.team;


import com.gdsc.toplearth_server.domain.entity.team.Member;
import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
import lombok.Builder;

@Builder
public record TeamMemberResponseDto(
        Long id,
        String name,
        ETeamRole role,
        Boolean isActive
) {
    public static TeamMemberResponseDto of(Member member) {
        return TeamMemberResponseDto.builder()
                .id(member.getId())
                .name(member.getUser().getNickname())
                .role(member.getTeamRole())
                .isActive(member.getIsActive())
                .build();
    }
}
