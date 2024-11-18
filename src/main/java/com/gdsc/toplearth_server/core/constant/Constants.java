package com.gdsc.toplearth_server.core.constant;

import java.util.List;

public final class Constants {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String USER_ID_CLAIM_NAME = "userId";
    public static final String USER_ROLE_CLAIM_NAME = "userRole";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    public static final String APPLICATION_FORM_URLENCODED_WITH_CHARSET = "application/x-www-form-urlencoded;charset=utf-8";
    public static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";

    // 인증이 필요 없는 URL
    public static List<String> NO_NEED_AUTH_URLS = List.of(
            "/api/v1/test/**"
    );
}
