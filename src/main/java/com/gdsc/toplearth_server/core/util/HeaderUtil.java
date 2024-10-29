package com.gdsc.toplearth_server.core.util;


import ch.qos.logback.core.util.StringUtil;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class HeaderUtil {
    public static Optional<String> refineHeader(HttpServletRequest request, String headerName, String prefix) {
        String token = request.getHeader(headerName);

        if(!StringUtils.hasText(token) || !token.startsWith(prefix)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST_HEAD);
        }

        return Optional.ofNullable(token.substring(prefix.length()));
    }
}
