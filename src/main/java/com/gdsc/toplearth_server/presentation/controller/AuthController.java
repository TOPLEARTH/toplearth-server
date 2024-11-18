package com.gdsc.toplearth_server.presentation.controller;

import com.gdsc.toplearth_server.application.service.AuthLoginService;
import com.gdsc.toplearth_server.core.common.CommonResponseDto;
import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.presentation.request.user.FcmTokenRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    private final AuthLoginService authLoginService;
    /**
     * 카카오 로그인
     */
    @PostMapping("/login/kakao")
    public CommonResponseDto<?> kakaoLogin(
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER) String kakaoAccessToken,
            @RequestBody FcmTokenRequestDto fcmTokenRequestDto
    ) {
        log.info("Kakao authToken : {}", kakaoAccessToken);
        return CommonResponseDto.ok(authLoginService.kakaoLogin(kakaoAccessToken, fcmTokenRequestDto));
    }

    /**
     * 애플 로그인
     */
    @PostMapping("/login/apple")
    public CommonResponseDto<?> appleLogin(
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER) String appleIdToken,
            @RequestBody FcmTokenRequestDto fcmTokenRequestDto
    ) {
        log.info("Apple authToken : {}", appleIdToken);
        return CommonResponseDto.ok(authLoginService.appleLogin(appleIdToken, fcmTokenRequestDto));
    }
}
