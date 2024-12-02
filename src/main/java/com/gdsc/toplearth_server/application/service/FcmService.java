package com.gdsc.toplearth_server.application.service;

import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.team.Member;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
import com.gdsc.toplearth_server.infrastructure.repository.matching.MatchingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.team.MemberRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.team.TeamRepositoryImpl;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FcmService {
    private final MemberRepositoryImpl memberRepository;
    private final TeamRepositoryImpl teamRepository;
    private final MatchingRepositoryImpl matchingRepository;

    public void randomMatchingStart(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
        List<Member> members = memberRepository.findByTeamAndTeamRole(team, ETeamRole.MEMBER);

        Member leader = memberRepository.findByTeamRoleAndTeam(ETeamRole.LEADER, team)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        members.forEach(member -> sendMessage(
                "랜덤 매칭 시작!!",
                String.format("%s님에 의해서 매칭이 시작되었습니다.", leader.getUser().getNickname()),
                member.getUser().getFcmToken()
        ));
    }

    public void matchingFinish(Long teamId, Long opponentId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        Team opponentTeam = teamRepository.findById(opponentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        List<Member> members = memberRepository.findByTeam(team);

        Matching matching = matchingRepository.findByTeamAndEndedAtIsNull(team)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MATCH));

        members.forEach(member -> sendMatchingInfoMessage(
                "매칭 완료!!",
                String.format("%s팀과의 대결이 시작되었습니다.", opponentTeam.getName()),
                member.getUser().getFcmToken(),
                matching.getId(),
                matching.getOpponentTeam().getName()
        ));
    }

    public void vsFinish(Long teamId, Long opponentId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        Team opponentTeam = teamRepository.findById(opponentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        List<Member> members = memberRepository.findByTeam(team);

        Matching matching = matchingRepository.findByTeamAndEndedAtIsNull(team)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MATCH));

        members.forEach(member -> sendMatchingInfoMessage(
                "대결 종료",
                String.format("%s팀과의 대결에서 %s하였습니다.", opponentTeam.getName(), matching.getWinFlag() ? "승리" : "패배"),
                member.getUser().getFcmToken(),
                matching.getId(),
                matching.getOpponentTeam().getName()
        ));
    }

    public void selectedMatching(Long teamId, Long opponentId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
        List<Member> ourMembers = memberRepository.findByTeamAndTeamRole(team, ETeamRole.MEMBER);
        Member leader = memberRepository.findByTeamRoleAndTeam(ETeamRole.LEADER, team)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Team opponentTeam = teamRepository.findById(opponentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        List<Member> opponentMembers = memberRepository.findByTeam(opponentTeam);

        ourMembers.forEach(ourMember -> sendMessage(
                "지정 매칭 요청!!",
                String.format("%s님에 의해서 %s에게 지정매칭을 요청했습니다.", leader.getUser().getMember(), opponentTeam.getName()),
                ourMember.getUser().getFcmToken()
        ));

        opponentMembers.forEach(opponentMember -> sendMessage(
                "지정 매칭 요청!!",
                String.format("%s팀에게 매칭 요청이 왔습니다.", team.getName()),
                opponentMember.getUser().getFcmToken()
        ));

    }


    //teamId는 수락을 해준팀 opponentId는 맨처음 지정 요청을 보낸팀
    public void acceptMatching(Long teamId, Long opponentId) {
        Team ourTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
        Team opponentTeam = teamRepository.findById(opponentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        List<Member> members = memberRepository.findByTeam(ourTeam, opponentTeam);

        members.forEach(ourMember -> sendMessage(
                "지정 매칭 완료!!",
                String.format("%s팀과 %s팀의 매칭이 성사되었습니다. 정각에 대결이 시작됩니다.", ourTeam.getName(), opponentTeam.getName()),
                ourMember.getUser().getFcmToken()
        ));
    }

    //teamId는 수락을 해준팀 opponentId는 맨처음 지정 요청을 보낸팀
    public void refuseMatching(Long teamId, Long opponentId) {
        Team ourTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
        Team opponentTeam = teamRepository.findById(opponentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        List<Member> members = memberRepository.findByTeam(ourTeam, opponentTeam);

        members.forEach(ourMember -> sendMessage(
                "지정 매칭 거절!!",
                String.format("%s팀에서 거절하여 매칭에 실패했습니다.", ourTeam.getName()),
                ourMember.getUser().getFcmToken()
        ));

    }

    public void sendMatchingInfoMessage(String title, String body, String token, Long matchingId, String opponentName) {
        Map<String, String> putData = new HashMap<>();
        putData.put("matchingId", String.valueOf(matchingId));
        putData.put("opponentName", opponentName);
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putAllData(putData)
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.SERVER_ERROR);
        }
        System.out.println("message " + response);
    }


    public void sendMessage(String title, String body, String token) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                //.putData("mingiId", "mingi_jeok")
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.SERVER_ERROR);
        }
        System.out.println("message " + response);
    }
}
