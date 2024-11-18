package com.gdsc.toplearth_server.domain.repository;

import com.gdsc.toplearth_server.domain.entity.user.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
}
