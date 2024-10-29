package com.gdsc.toplearth_server.core.constant;

import java.util.List;

public final class Constants {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String USER_ID_CLAIM_NAME = "userId";
    public static final String USER_ROLE_CLAIM_NAME = "userRole";

    // 인증이 필요 없는 URL
    public static List<String> NO_NEED_AUTH_URLS = List.of(
            "/test/signin/**"
    );
}
