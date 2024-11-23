package com.gdsc.toplearth_server.core.security.filter;

import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        boolean isException = false;
        try {
            filterChain.doFilter(request, response);
        } catch (SecurityException e) {
            request.setAttribute("exception", ErrorCode.ACCESS_DENIED_ERROR);
            isException = true;
        } catch (MalformedJwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_MALFORMED);
            isException = true;
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_TYPE);
            isException = true;
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_EXPIRED);
            isException = true;
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_UNSUPPORTED);
            isException = true;
        } catch (JwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_UNKNOWN);
            isException = true;
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.NOT_FOUND_USER);
            isException = true;
        }

        if (isException) {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Constants.NO_NEED_AUTH_URLS.stream().anyMatch(request.getRequestURI()::startsWith);
    }
}