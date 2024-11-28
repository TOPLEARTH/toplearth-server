package com.gdsc.toplearth_server.application.service.matching;

import com.gdsc.toplearth_server.application.service.FcmService;
import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.infrastructure.message.TeamInfoMessage;
import com.gdsc.toplearth_server.infrastructure.repository.matching.MatchingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.team.TeamRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {
    private final RabbitTemplate rabbitTemplate;
    private final MatchingRepositoryImpl matchingRepositoryImpl;
    private final TeamRepositoryImpl teamRepositoryImpl;
    private final FcmService fcmService;

    // 랜덤 매칭 요청을 큐에 추가, Producer 역할
    public void addRandomMatchingRequest(TeamInfoMessage teamInfoMessage) {
        log.info("Adding random matching request for teamId: {}", teamInfoMessage.teamId());
        // Queue 추가
        rabbitTemplate.convertAndSend(Constants.MATCHING_EXCHANGE_NAME, Constants.MATCHING_ROUTING_KEY,
                teamInfoMessage);
        // 매칭 시작 알람 전송
        fcmService.randomMatchingStart(teamInfoMessage.teamId());
    }

    // 지정 매칭 요청
    public void requestTeamMatching(Long opponentTeamId, TeamInfoMessage teamInfoMessage) {
        fcmService.selectedMatching(teamInfoMessage.teamId(), opponentTeamId);
    }

    // 수락 시 매칭 요청을 큐에 추가, Producer 역할
    public void addTeamMatchingRequest(Long opponentTeamId, TeamInfoMessage teamInfoMessage) {
        log.info("Adding team matching request for teamId: {}", teamInfoMessage.teamId());
        rabbitTemplate.convertAndSend(Constants.MATCHING_EXCHANGE_NAME, Constants.MATCHING_ROUTING_KEY,
                teamInfoMessage);
        // TODO: 매칭 요청 알람 보내기

    }

    /**
     * RabbitMQ에서 랜덤 매칭 요청 처리
     */
    public void processRandomMatchingRequests(List<TeamInfoMessage> requests) {
        log.info("Processing matching requests...");

        if (requests.size() < 2) {
            log.warn("Not enough teams for matching. At least two teams are required.");
            return; // 짝이 없는 경우 처리 중단
        }

        // 팀 매칭 처리
        List<Matching> matchedPairs = new ArrayList<>();
        for (int i = 0; i < requests.size() - 1; i += 2) {
            Long team1Id = requests.get(i).teamId();
            Long team2Id = requests.get(i + 1).teamId();

            Team team = teamRepositoryImpl.findById(team1Id)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
            Team opponentTeam = teamRepositoryImpl.findById(team2Id)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

            Matching team1Matching = createMatching(team, opponentTeam);
            Matching team2Matching = createMatching(opponentTeam, team);

            matchedPairs.add(team1Matching);
            matchedPairs.add(team2Matching);
        }

        if (!matchedPairs.isEmpty()) {
            matchingRepositoryImpl.saveAll(matchedPairs);
            log.info("{} matchings saved successfully.", matchedPairs.size());
        }

        // 매칭 완료 알람 전송
        matchedPairs
                .forEach(matching -> {
                    Team team = matching.getTeam();
                    Team opponentTeam = matching.getOpponentTeam();
                    fcmService.matchingFinish(team.getId(), opponentTeam.getId());
                    fcmService.matchingFinish(opponentTeam.getId(), team.getId());
                });
    }

    /**
     * 매칭 생성 메서드
     */
    private Matching createMatching(Team team, Team opponentTeam) {
        Matching matching = Matching.fromTeamEntities(team, opponentTeam);
        log.info("Matching created: {} vs {}", team, opponentTeam);
        return matching;
    }

}
