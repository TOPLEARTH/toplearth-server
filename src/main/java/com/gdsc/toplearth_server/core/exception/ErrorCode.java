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
    TOKEN_MALFORMED("40008", HttpStatus.UNAUTHORIZED, "잘못된 토큰을 사용했어요."),
    TOKEN_TYPE("40009", HttpStatus.UNAUTHORIZED, "잘못된 토큰타입을 사용했어요."),


    /**
     * 401** Unauthorized
     */
    FAILURE_LOGIN("40100", HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    TOKEN_EXPIRED("40101", HttpStatus.UNAUTHORIZED,"토큰이 만료되었습니다,"),
    TOKEN_UNSUPPORTED("40102", HttpStatus.UNAUTHORIZED, "지원되지 않는 형식의 토큰이에요"),
    TOKEN_UNKNOWN("40103", HttpStatus.UNAUTHORIZED, "토큰을 알수 없어요"),

    /**
     * 403** Access Denied
     */

    ACCESS_DENIED_ERROR("40300", HttpStatus.FORBIDDEN, "액세스 권한이 없습니다."),
    ENPTY_AUTHENTICATION("40301", HttpStatus.FORBIDDEN, "인증 토큰이 비었습니다."),
    /**
     * 404** Not Found
     */
    NOT_FOUND_END_POINT("40400", HttpStatus.NOT_FOUND, "존재하지 않는 엔드포인트입니다."),
    NOT_FOUND_USER("40401", HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),

    /**
     * 405** Method Not Allowed
     */
    METHOD_NOT_ALLOWED("40500", HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),

    /**
     * 500** Server Error
     */
    SERVER_ERROR("50000", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
