package com.gdsc.toplearth_server.application.dto.user;

import com.gdsc.toplearth_server.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserInfoResponseDto(
        String userId,  // 유저 UUID
        String email,   // 유저 이메일
        String nickname,    // 유저 닉네임
        String totalKilometers,  // 유저 총 이동거리
        String targetKilometers, // 유저 목표 이동거리
        String creditInfo  // 유저 크레딧 정보
) {
    public static UserInfoResponseDto fromUserEntity(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getId().toString())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .totalKilometers(user.getTotalDistance().toString())
                .targetKilometers(user.getGoalDistance().toString())
                .creditInfo(user.getCredit().toString())
                .build();
    }
}
