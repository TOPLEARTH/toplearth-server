package com.gdsc.toplearth_server.infrastructure.repository.team;

import com.gdsc.toplearth_server.domain.entity.team.Member;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
import com.gdsc.toplearth_server.domain.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepositoryImpl extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndTeam(Long memberId, Team team);

    Optional<Member> findByUserAndTeam(User user, Team team);

    Integer countByTeam(Team team);

    @Query("SELECT CASE WHEN COUNT(m.id) > 0 THEN true ELSE false END FROM Member m WHERE m.user = :user")
    boolean existsByUser(User user);

    List<Member> findByTeam(Team team);

    Optional<Member> findByTeamRoleAndTeam(ETeamRole eTeamRole, Team team);
}
