package com.gdsc.toplearth_server.application.dto.oauth;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record OAuth2UserInfoResponseDto(
        String oAuthId,    // 애플 sub, 카카오 id
        String email
) {
    public static OAuth2UserInfoResponseDto of(String oAuthId, String email) {
        return OAuth2UserInfoResponseDto.builder()
                .oAuthId(oAuthId)
                .email(email)
                .build();
    }
}
