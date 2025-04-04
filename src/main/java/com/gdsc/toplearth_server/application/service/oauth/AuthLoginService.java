package com.gdsc.toplearth_server.application.service.oauth;

import com.gdsc.toplearth_server.application.dto.oauth.OAuth2UserInfoResponseDto;
import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.core.security.JwtDto;
import com.gdsc.toplearth_server.core.util.JwtUtil;
import com.gdsc.toplearth_server.core.util.NicknameUtil;
import com.gdsc.toplearth_server.core.util.OAuth2Util;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.domain.entity.user.type.ELoginProvider;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import com.gdsc.toplearth_server.presentation.request.user.FcmTokenRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthLoginService {
    private final UserRepositoryImpl userRepositoryImpl;
    private final OAuth2Util oAuth2Util;
    private final JwtUtil jwtUtil;

    @Transactional
    public JwtDto kakaoLogin(String kakaoAccessToken, FcmTokenRequestDto fcmTokenRequestDto) {
        String fcmToken = fcmTokenRequestDto.fcmToken();
        OAuth2UserInfoResponseDto OAuth2UserInfo = getOAuth2UserInfo(
                ELoginProvider.KAKAO.toString(),
                refineToken(kakaoAccessToken)
        );
        User user = userRepositoryImpl.findBySocialId(OAuth2UserInfo.oAuthId())
                .orElseGet(() -> joinUser(
                        OAuth2UserInfo,
                        ELoginProvider.KAKAO,
                        fcmToken
                ));
        log.info("fcm Token!!!: {}", fcmToken);
        user.updateFcmToken(fcmToken);
        return jwtUtil.generateTokens(user.getId(), user.getUserRole());
    }

    @Transactional
    public JwtDto appleLogin(String appleIdToken, FcmTokenRequestDto fcmTokenRequestDto) {
        String fcmToken = fcmTokenRequestDto.fcmToken();
        OAuth2UserInfoResponseDto OAuth2UserInfo = getOAuth2UserInfo(
                ELoginProvider.APPLE.toString(),
                refineToken(appleIdToken)
        );
        User user = userRepositoryImpl.findBySocialId(OAuth2UserInfo.oAuthId())
                .orElseGet(() -> joinUser(
                        OAuth2UserInfo,
                        ELoginProvider.APPLE,
                        fcmToken
                ));
        log.info("fcm Token!!!: {}", fcmToken);
        user.updateFcmToken(fcmToken);
        return jwtUtil.generateTokens(user.getId(), user.getUserRole());
    }

    private User joinUser(OAuth2UserInfoResponseDto OAuth2UserInfo, ELoginProvider provider, String fcmToken) {
        return userRepositoryImpl.save(
                User.toUserEntity(
                        OAuth2UserInfo.oAuthId(),
                        NicknameUtil.generateRandomNickname(),
                        EUserRole.USER,
                        provider,
                        fcmToken
                )
        );
    }

    private OAuth2UserInfoResponseDto getOAuth2UserInfo(String provider, String accessToken) {
        if (provider.equals(ELoginProvider.KAKAO.toString())) {
            return oAuth2Util.getKakaoUserInfo(accessToken);
        } else if (provider.equals(ELoginProvider.APPLE.toString())) {
            return oAuth2Util.getAppleUserInfo(accessToken);
        } else {
            throw new CustomException(ErrorCode.INVALID_OAUTH2_PROVIDER);
        }
    }

    private String refineToken(String accessToken) {
        return accessToken.replaceFirst("^" + Constants.BEARER_PREFIX, "");
    }
}
