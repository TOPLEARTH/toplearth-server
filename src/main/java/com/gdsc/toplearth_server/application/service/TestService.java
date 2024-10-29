package com.gdsc.toplearth_server.application.service;

import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.core.security.JwtDto;
import com.gdsc.toplearth_server.core.util.JwtUtil;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TestService {
    private final UserRepositoryImpl userRepository;
    private final JwtUtil jwtUtil;

    public JwtDto signIn(String username, EUserRole role) {
        User user = User.builder()
                .email(username)
                .userRole(role)
                .nickname("aaa")
                .build();

        userRepository.save(user);

        log.info("userId" + user.getId());

        return jwtUtil.generateTokens(user.getId(), role);
    }


}
