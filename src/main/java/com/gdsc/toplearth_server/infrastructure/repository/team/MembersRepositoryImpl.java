package com.gdsc.toplearth_server.infrastructure.repository.team;

import com.gdsc.toplearth_server.domain.entity.team.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembersRepositoryImpl extends JpaRepository<Members, Long> {
}
