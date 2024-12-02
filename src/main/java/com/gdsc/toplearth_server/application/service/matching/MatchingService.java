package com.gdsc.toplearth_server.application.service.matching;

import com.gdsc.toplearth_server.application.dto.matching.MatchingStatusResponseDto;
import com.gdsc.toplearth_server.application.dto.matching.RecentMatchingInfo;
import com.gdsc.toplearth_server.application.dto.plogging.MatchingRecentPloggingResponseDto;
import com.gdsc.toplearth_server.application.service.FcmService;
import com.gdsc.toplearth_server.core.constant.Constants;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.matching.type.EMatchingStatus;
import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.team.Member;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.infrastructure.message.TeamInfoMessage;
import com.gdsc.toplearth_server.infrastructure.repository.matching.MatchingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.team.TeamRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import com.gdsc.toplearth_server.presentation.request.matching.VSFinishRequestDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {
    private final RabbitTemplate rabbitTemplate;
    private final MatchingRepositoryImpl matchingRepositoryImpl;
    private final TeamRepositoryImpl teamRepositoryImpl;
    private final FcmService fcmService;
    private final UserRepositoryImpl userRepositoryImpl;
    private final PloggingRepositoryImpl ploggingRepositoryImpl;

    // 랜덤 매칭 요청을 큐에 추가, Producer 역할
    @Transactional
    public void addRandomMatchingRequest(UUID userId, TeamInfoMessage teamInfoMessage) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        if (!user.getMember().getTeamRole().equals(ETeamRole.LEADER)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED_LEADER); // 리더가 아닌 경우 처리 중단
        }

        log.info("Adding random matching request for teamId: {}", teamInfoMessage.teamId());
        // Queue 추가
        try {
            rabbitTemplate.convertAndSend(Constants.MATCHING_EXCHANGE_NAME, Constants.MATCHING_ROUTING_KEY,
                    teamInfoMessage);
            log.info("Message successfully sent to RabbitMQ: {}", teamInfoMessage);
        } catch (Exception e) {
            log.error("Failed to send message to RabbitMQ: {}", e.getMessage(), e);
        }

//        rabbitTemplate.convertAndSend(Constants.MATCHING_EXCHANGE_NAME, Constants.MATCHING_ROUTING_KEY,
//                teamInfoMessage);
        // 매칭 시작 알람 전송
        fcmService.randomMatchingStart(teamInfoMessage.teamId());
    }

    // 지정 매칭 요청
    @Transactional
    public void requestTeamMatching(UUID userId, Long opponentTeamId, TeamInfoMessage teamInfoMessage) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        if (!user.getMember().getTeamRole().equals(ETeamRole.LEADER)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED_LEADER); // 리더가 아닌 경우 처리 중단
        }
        // 매칭 요청 알람 전송
        fcmService.selectedMatching(teamInfoMessage.teamId(), opponentTeamId);
    }

    // 수락 시 매칭 요청을 큐에 추가, Producer 역할
    @Transactional
    public void addTeamMatchingRequest(UUID userId, Long opponentTeamId, TeamInfoMessage teamInfoMessage) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        if (!user.getMember().getTeamRole().equals(ETeamRole.LEADER)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED_LEADER); // 리더가 아닌 경우 처리 중단
        }
        log.info("Adding team matching request for teamId: {}", teamInfoMessage.teamId());
        rabbitTemplate.convertAndSend(Constants.MATCHING_EXCHANGE_NAME, Constants.MATCHING_ROUTING_KEY,
                teamInfoMessage);

        // 상대팀과 우리팀에게 매칭 성사 알람 전송
        fcmService.acceptMatching(teamInfoMessage.teamId(), opponentTeamId);
    }

    @Transactional
    public void rejectTeamMatching(UUID userId, Long opponentTeamId, TeamInfoMessage teamInfoMessage) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        if (!user.getMember().getTeamRole().equals(ETeamRole.LEADER)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED_LEADER); // 리더가 아닌 경우 처리 중단
        }
        log.info("Rejecting team matching request for teamId: {}", teamInfoMessage.teamId());
        fcmService.refuseMatching(teamInfoMessage.teamId(), opponentTeamId);
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
                });
    }

    /**
     * 매칭 상태값 반환 메서드
     */
    @Transactional(readOnly = true)
    public MatchingStatusResponseDto getMatchingStatus(UUID userId) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 소속된 팀이 없을 때
        EMatchingStatus status;
        Member member = user.getMember() == null ? null : user.getMember();
        if (member == null) {
            status = EMatchingStatus.NOT_JOINED;
            return MatchingStatusResponseDto.of(status);
        }

        // 소속된 팀은 있지만, 매칭이 없을 때
        Team team = member.getTeam();
        Optional<Matching> matching = matchingRepositoryImpl.findByTeamAndEndedAtIsNull(team);
        if (matching.isEmpty()) {
            status = EMatchingStatus.DEFAULT;
            return MatchingStatusResponseDto.of(status);
        }

        // 매칭이 잡혔을 때
        if (matching.get().getEndedAt() == null) {
            status = matching.get().getPloggings().stream()
                    .anyMatch(plogging -> plogging.getTeam() == team && plogging.getEndedAt() == null)
                    ? EMatchingStatus.PLOGGING
                    : EMatchingStatus.MATCHED;
            return MatchingStatusResponseDto.of(status);
        }

        // 대결 종료
        status = EMatchingStatus.FINISHED;
        return MatchingStatusResponseDto.of(status);
    }

    /**
     * 매칭 생성 메서드
     */
    private Matching createMatching(Team team, Team opponentTeam) {
        Matching matching = Matching.fromTeamEntities(team, opponentTeam);
        log.info("Matching created: {} vs {}", team, opponentTeam);
        return matching;
    }

    /**
     * 매칭 종료 처리
     */
    @Transactional
    public void finishVS(UUID userId, Long matchingId, VSFinishRequestDto vsFinishRequestDto) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Matching matching = matchingRepositoryImpl.findById(matchingId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MATCH));

        matching.finishVS(
                vsFinishRequestDto.winFlag(),
                vsFinishRequestDto.competitionScore(),
                vsFinishRequestDto.totalPickUpCnt()
        );

        matchingRepositoryImpl.save(matching);

        fcmService.vsFinish(matching.getTeam().getId(), matching.getOpponentTeam().getId());
        fcmService.vsFinish(matching.getOpponentTeam().getId(), matching.getTeam().getId());

        log.info("Matching ID: {} has been finished.", matching.getId());
    }

    @Transactional(readOnly = true)
    public RecentMatchingInfo recentPlogging(UUID userId) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Team ourTeam = user.getMember().getTeam();

        Matching ourTeamMatching = matchingRepositoryImpl.findFirstByTeamOrderByStartedAtDesc(ourTeam)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MATCH));

        Matching opponentTeamMatching = matchingRepositoryImpl.findFirstByTeamOrderByStartedAtDesc(
                        ourTeamMatching.getOpponentTeam(), ourTeam)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MATCH));

        List<Plogging> ploggings = ploggingRepositoryImpl.findByOurTeamMatchingWithMatching(ourTeamMatching,
                opponentTeamMatching);

        List<MatchingRecentPloggingResponseDto> matchingRecentPloggingResponseDtos = ploggings.stream()
                .map(plogging -> MatchingRecentPloggingResponseDto.fromPloggingEntity(plogging,
                        plogging.getPloggingImages()))
                .toList();
        return RecentMatchingInfo.of(matchingRecentPloggingResponseDtos);
    }
}
