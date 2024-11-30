package com.gdsc.toplearth_server.core.interceptor.pre;

import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HttpUserIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new CustomException(ErrorCode.EMPTY_AUTHENTICATION);
        }


        request.setAttribute("userId", authentication.getName());

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
