package com.gdsc.toplearth_server.infrastructure.repository.user;

import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import com.gdsc.toplearth_server.domain.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepositoryImpl extends JpaRepository<User, UUID>, UserRepository {
    @Query("SELECT u FROM User u WHERE (:nickname IS NULL OR u.nickname LIKE CONCAT('%', :nickname, '%')) AND u.userRole = :role")
    Page<User> searchUserList(@Param("nickname") String nickname,
                              @Param("role") EUserRole userRole,
                              Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.socialId = :socialId")
    Optional<User> findBySocialId(String socialId);
}
