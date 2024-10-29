package com.gdsc.toplearth_server.core.security;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Builder
public record JwtDto(
        @NotNull(message = "accessToken이 널입니다.")
        String accessToken,
        @NotNull(message = "refreshToken이 널입니다.")
        String refreshToken
) {
    public JwtDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
