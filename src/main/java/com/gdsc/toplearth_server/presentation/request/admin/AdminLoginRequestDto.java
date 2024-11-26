package com.gdsc.toplearth_server.presentation.request.admin;

public record AdminLoginRequestDto(
        String loginId,
        String password
) {
}
