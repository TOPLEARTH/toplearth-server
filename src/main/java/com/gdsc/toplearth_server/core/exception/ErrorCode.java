package com.gdsc.toplearth_server.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * 400** Bad Request
     */
    INVALID_REQUEST_PARAMETER("40000", HttpStatus.BAD_REQUEST, "Invalid request parameter provided."),
    INVALID_REQUEST_BODY("40001", HttpStatus.BAD_REQUEST, "Invalid request body provided."),
    INVALID_METHOD_ARGUMENT("40002", HttpStatus.BAD_REQUEST, "Invalid method argument provided."),
    MISSING_REQUEST_PARAMETER("40003", HttpStatus.BAD_REQUEST, "Required parameter is missing."),
    ARGUMENT_TYPE_MISMATCH("40004", HttpStatus.BAD_REQUEST, "Argument Type mismatch."),
    MISSING_REQUEST_PART("40005", HttpStatus.BAD_REQUEST, "Missing request part."),
    UNSUPPORTED_MEDIA_TYPE("40006", HttpStatus.BAD_REQUEST, "Unsupported Media Type."),
    INVALID_REQUEST_HEAD("40007", HttpStatus.BAD_REQUEST, "Invalid request head provided."),
    INVALID_OAUTH2_PROVIDER("40008", HttpStatus.BAD_REQUEST, "잘못된 OAuth2 제공자입니다."),

    /**
     * 401** Unauthorized
     */
    FAILURE_LOGIN("40100", HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    TOKEN_EXPIRED("40101", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다,"),
    TOKEN_UNSUPPORTED("40102", HttpStatus.UNAUTHORIZED, "지원되지 않는 형식의 토큰이에요"),
    TOKEN_UNKNOWN("40103", HttpStatus.UNAUTHORIZED, "토큰을 알수 없어요"),
    TOKEN_MALFORMED("40104", HttpStatus.UNAUTHORIZED, "잘못된 토큰을 사용했어요."),
    TOKEN_TYPE("40105", HttpStatus.UNAUTHORIZED, "잘못된 토큰타입을 사용했어요."),
    INVALID_APPLE_IDENTITY_TOKEN_ERROR("40106", HttpStatus.UNAUTHORIZED, "잘못된 Apple Identity 토큰입니다."),
    EXPIRED_APPLE_IDENTITY_TOKEN_ERROR("40107", HttpStatus.UNAUTHORIZED, "만료된 Apple Identity 토큰입니다."),
    INVALID_APPLE_PUBLIC_KEY_ERROR("40108", HttpStatus.UNAUTHORIZED, "잘못된 Apple 공개키입니다."),

    /**
     * 403** Access Denied
     */
    ACCESS_DENIED_ERROR("40300", HttpStatus.FORBIDDEN, "액세스 권한이 없습니다."),
    ENPTY_AUTHENTICATION("40301", HttpStatus.FORBIDDEN, "인증 토큰이 비었습니다."),
    ACCESS_DENIED_LEADER("40301", HttpStatus.FORBIDDEN, "당신은 리더가 아닙니다."),

    /**
     * 404** Not Found
     */
    NOT_FOUND_END_POINT("40400", HttpStatus.NOT_FOUND, "존재하지 않는 엔드포인트입니다."),
    NOT_FOUND_USER("40401", HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),
    NOT_FOUND_TEAM("40402", HttpStatus.NOT_FOUND, "해당 팀이 존재하지 않습니다."),
    NOT_FOUND_MEMBER("40403", HttpStatus.NOT_FOUND, "해당 팀 멤버가 존재하지 않습니다."),
    NOT_FOUND_REPORT("40404", HttpStatus.NOT_FOUND, "해당 신고가 존재하지 않습니다."),

    /**
     * 405** Method Not Allowed
     */
    METHOD_NOT_ALLOWED("40500", HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),

    /**
     * 409** Conflict
     */
    CONFLICT_TEAM_NAME("40900", HttpStatus.CONFLICT, "팀 이름이 중복입니다."),
    CONFLICT_TEAM_COUNT("40901", HttpStatus.CONFLICT, "팀 정원을 초과하였습니다."),
    CONFLICT_TEAM_BUILDING("40902", HttpStatus.CONFLICT, "이미 팀에 가입되어 있어 다른 팀을 생성하거나 가입할 수 없습니다.."),

    /**
     * 500** Server Error
     */
    SERVER_ERROR("50000", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
