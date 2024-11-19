package com.gdsc.toplearth_server.core.security.filter;


import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.core.security.service.CustomUserDetailService;
import com.gdsc.toplearth_server.core.security.info.UserPrincipal;
import com.gdsc.toplearth_server.core.util.HeaderUtil;
import com.gdsc.toplearth_server.core.util.JwtUtil;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = HeaderUtil.refineHeader(request, Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REQUEST_HEAD));

        Claims claims = jwtUtil.validateToken(token);

        UUID userId = UUID.fromString(claims.get(Constants.USER_ID_CLAIM_NAME, String.class));
        EUserRole userRole = EUserRole.valueOf(String.valueOf(claims.get(Constants.USER_ROLE_CLAIM_NAME, String.class)));

        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailService.loadUserById(userId);

        if(!userPrincipal.getUserRole().equals(userRole)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED_ERROR);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userPrincipal,
                null,
                userPrincipal.getAuthorities()
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Constants.NO_NEED_AUTH_URLS.stream().anyMatch(request.getRequestURI()::startsWith);
    }
}
