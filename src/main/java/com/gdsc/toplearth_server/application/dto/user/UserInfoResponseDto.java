package com.gdsc.toplearth_server.application.dto.user;

import com.gdsc.toplearth_server.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserInfoResponseDto(
        String userId,  // 유저 UUID
        String socialId,   // 유저 소셜 아이디
        String nickname,    // 유저 닉네임
        Double totalKilometers,  // 유저 총 이동거리
        Double targetKilometers, // 유저 목표 이동거리
        Integer creditInfo,  // 유저 크레딧 정보
        Boolean isJoinedTeam  // 유저 팀 가입 여부
) {
    public static UserInfoResponseDto fromUserEntity(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getId().toString())
                .socialId(user.getSocialId())
                .nickname(user.getNickname())
                .totalKilometers(user.getTotalDistance())
                .targetKilometers(user.getGoalDistance())
                .creditInfo(user.getCredit())
                .isJoinedTeam(user.checkJoinedTeam())
                .build();
    }
}
