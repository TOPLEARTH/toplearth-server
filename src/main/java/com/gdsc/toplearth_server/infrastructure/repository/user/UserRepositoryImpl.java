package com.gdsc.toplearth_server.infrastructure.repository.user;

import com.gdsc.toplearth_server.domain.entity.user.Users;
import com.gdsc.toplearth_server.domain.repository.UserRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryImpl extends JpaRepository<Users, UUID>, UserRepository {

}
