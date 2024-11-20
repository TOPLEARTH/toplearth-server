package com.gdsc.toplearth_server.application.dto.admin;

import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
import com.gdsc.toplearth_server.domain.entity.user.User;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReadUserResponseDto(
        UUID userId,
        //String email,
        String nickname,
        String profileImageUrl,
        ETeamRole eTeamRole
) {
    public static ReadUserResponseDto of(User user) {
        return ReadUserResponseDto.builder()
                .userId(user.getId())
                //.email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .eTeamRole(user.getMember() != null ? user.getMember().getETeamRole() : null)
                .build();
    }
}
