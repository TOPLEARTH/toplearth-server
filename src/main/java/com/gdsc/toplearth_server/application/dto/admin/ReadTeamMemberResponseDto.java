package com.gdsc.toplearth_server.application.dto.admin;

import com.gdsc.toplearth_server.domain.entity.team.Member;
import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
import lombok.Builder;

@Builder
public record ReadTeamMemberResponseDto(
        Long memberId,
        String nickname,
        ETeamRole eTeamRole
) {
    public static ReadTeamMemberResponseDto of(Member member) {
        return ReadTeamMemberResponseDto.builder()
                .memberId(member.getId())
                .nickname(member.getUser().getNickname())
                .eTeamRole(member.getETeamRole())
                .build();
    }
}
