package com.gdsc.toplearth_server.core.common;

import com.gdsc.toplearth_server.core.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ExceptionDto {
    private final String code;
    private final String message;

    public ExceptionDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(errorCode);
    }
}
