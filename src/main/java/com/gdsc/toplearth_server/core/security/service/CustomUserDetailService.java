package com.gdsc.toplearth_server.core.security.service;

import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.core.security.info.UserPrincipal;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepositoryImpl userRepositoryImpl;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return null;
    }

    public UserDetails loadUserById(UUID id) {
        User user = userRepositoryImpl.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return UserPrincipal.create(user);
    }

}
