package com.gdsc.toplearth_server.application.service.matching;

import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.infrastructure.repository.matching.MatchingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.team.TeamRepositoryImpl;
import com.gdsc.toplearth_server.presentation.request.matching.MatchingRequestDto;
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

    // 랜덤 매칭 요청을 큐에 추가, Producer 역할
    public void addRandomMatchingRequest(MatchingRequestDto matchingRequestDto) {
        log.info("Adding random matching request for teamId: {}", matchingRequestDto.teamId());
        rabbitTemplate.convertAndSend(Constants.MATCHING_EXCHANGE_NAME, Constants.MATCHING_ROUTING_KEY, matchingRequestDto);
    }

    // 팀 지정 매칭 요청을 큐에 추가, Producer 역할
    public void addTeamMatchingRequest(Long teamId, MatchingRequestDto matchingRequestDto) {
        log.info("Adding team matching request for teamId: {}", matchingRequestDto.teamId());
        rabbitTemplate.convertAndSend(Constants.MATCHING_EXCHANGE_NAME, Constants.MATCHING_ROUTING_KEY, matchingRequestDto);

        // TODO: 매칭 완료 알람 보내기
    }

    /**
     * RabbitMQ에서 매칭 요청 처리
     */
    public void processMatchingRequests(List<MatchingRequestDto> requests) {
        log.info("Processing matching requests...");

        // 팀 매칭 처리
        List<Matching> matchedPairs = new ArrayList<>();
        for (int i = 0; i < requests.size() - 1; i += 2) {
            Long team1Id = Long.valueOf(requests.get(i).teamId());
            Long team2Id = Long.valueOf(requests.get(i + 1).teamId());
            Team team = teamRepositoryImpl.findById(team1Id).orElseThrow();
            Team opponentTeam = teamRepositoryImpl.findById(team2Id).orElseThrow();
            Matching team1Matching = createMatching(team, opponentTeam);
            Matching team2Matching = createMatching(opponentTeam, team);
            matchedPairs.add(team1Matching);
            matchedPairs.add(team2Matching);
        }

        if (!matchedPairs.isEmpty()) {
            matchingRepositoryImpl.saveAll(matchedPairs);
            log.info("{} matchings saved successfully.", matchedPairs.size());
        }

        // TODO: 매칭 완료 알람 보내기
    }

    /**
     * 매칭 생성 메서드
     */
    private Matching createMatching(Team team1Id, Team team2Id) {
        Matching matching = Matching.builder()
                .team(team1Id)
                .opponentTeam(team2Id)
                .build();
        log.info("Matching created: {} vs {}", team1Id, team2Id);
        return matching;
    }

}
