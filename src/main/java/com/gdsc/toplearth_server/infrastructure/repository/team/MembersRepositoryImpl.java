package com.gdsc.toplearth_server.infrastructure.repository.team;

import com.gdsc.toplearth_server.domain.entity.team.Members;
import com.gdsc.toplearth_server.domain.entity.team.Teams;
import com.gdsc.toplearth_server.domain.entity.user.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembersRepositoryImpl extends JpaRepository<Members, Long> {
    Optional<Members> findByIdAndTeam(Long memberId, Teams team);

    Optional<Members> findByUserAndTeam(Users user, Teams team);

    Integer countByTeam(Teams team);

    Boolean existsByUser(Users user);

    List<Members> findByTeam(Teams team);
}
