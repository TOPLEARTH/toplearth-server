package com.gdsc.toplearth_server.application.service.team;

import com.gdsc.toplearth_server.application.dto.team.CreateTeamResponseDto;
import com.gdsc.toplearth_server.application.dto.team.ReadTeamDistanceResponseDto;
import com.gdsc.toplearth_server.application.dto.team.ReadTeamLabelResponseDto;
import com.gdsc.toplearth_server.application.dto.team.ReadTeamResponseDto;
import com.gdsc.toplearth_server.application.dto.team.ReadTeamStatisticsResponseDto;
import com.gdsc.toplearth_server.application.dto.team.UpdateTeamCodeResponseDto;
import com.gdsc.toplearth_server.application.dto.team.UpdateTeamNameResponseDto;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import com.gdsc.toplearth_server.domain.entity.plogging.type.ELabel;
import com.gdsc.toplearth_server.domain.entity.team.Member;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.team.type.ETeamRole;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.infrastructure.repository.matching.MatchingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.team.MemberRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.team.TeamRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import com.gdsc.toplearth_server.presentation.request.team.CreateTeamRequestDto;
import com.gdsc.toplearth_server.presentation.request.team.UpdateTeamNameRequestDto;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TeamService {
    private final TeamRepositoryImpl teamsRepository;
    private final MemberRepositoryImpl membersRepository;
    private final UserRepositoryImpl userRepository;
    private final MatchingRepositoryImpl matchingRepository;
    private final PloggingRepositoryImpl ploggingRepository;

    //팀 네비게이션 바 팀 정보 조회
    @Transactional(readOnly = true)
    public ReadTeamResponseDto readTeam(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)); // 일단 user를 찾는다.

        Team team = user.getMember().getTeam(); // 그 해당하는 유저의 팀을 찾는다.

        Integer matchCnt = matchingRepository.countByTeam(team); // 유저가 속한 팀의 매치 횟수
        Integer winCnt = matchingRepository.countByTeamAndWinFlagIsTrue(team); // 유저가 속한 팀의 승리횟수

        List<Member> members = membersRepository.findByTeam(team); // 그 유저가 속한 팀의 모든 멤버들을 조회한다. //멤버 정보에 사용할 예정

        List<Plogging> ploggingList = ploggingRepository.findByYearAndTeam(team.getCreatedAt().getYear(),
                team); // 플로깅을 시작한 연도와 유저의 팀을 기준으로 플로깅 정보를 불러온다.

        Map<YearMonth, ReadTeamStatisticsResponseDto> memberMonthlyDataMap = ploggingList.stream()
                .collect(Collectors.groupingBy(
                        plogging -> YearMonth.from(plogging.getStartedAt()),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                ploggings -> {
                                    List<ReadTeamDistanceResponseDto> selectResponseDtos = members.stream()
                                            .map(member -> ReadTeamDistanceResponseDto.of(
                                                    member,
                                                    ploggings.stream()
                                                            .filter(p -> p.getUser().equals(member.getUser()))
                                                            .mapToDouble(Plogging::getDistance)
                                                            .sum()
                                            ))
                                            .collect(Collectors.toList());

                                    Map<ELabel, Long> labelCounts = ploggings.stream()
                                            .flatMap(plogging -> plogging.getPloggingImages().stream())
                                            .collect(Collectors.groupingBy(
                                                    PloggingImage::getELabel,
                                                    Collectors.counting()
                                            ));

                                    List<ReadTeamLabelResponseDto> readLabelResponseDtos = labelCounts.entrySet()
                                            .stream()
                                            .map(entry -> new ReadTeamLabelResponseDto(entry.getKey(),
                                                    entry.getValue().intValue()))
                                            .collect(Collectors.toList());

                                    return new ReadTeamStatisticsResponseDto(selectResponseDtos, readLabelResponseDtos);
                                }
                        )
                ));

        return ReadTeamResponseDto.of(team, matchCnt, winCnt, memberMonthlyDataMap);
    }

    //팀목록 검색
    @Transactional(readOnly = true)
    public List<ReadTeamResponseDto> searchTeam(String searchName) {
        List<Team> teams = teamsRepository.findByNameContaining(searchName);

        if (teams.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_TEAM);
        }

        return teams.stream()
                .map(team -> ReadTeamResponseDto.builder()
                        .teamId(team.getId())
                        .teamCode(team.getCode())
                        .teamName(team.getName())
                        .build()).toList();
    }

    //팀 이름 업데이트
    public UpdateTeamNameResponseDto updateTeamName(Long teamId, UpdateTeamNameRequestDto updateTeamNameRequestDto) {
        Team team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        team.updateName(updateTeamNameRequestDto.name());

        return UpdateTeamNameResponseDto.builder()
                .teamName(team.getName())
                .build();
    }

    //팀 코드 업데이트
    public UpdateTeamCodeResponseDto updateTeamCode(Long teamId) {
        Team team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        team.updateCode(codeGenerator());

        return UpdateTeamCodeResponseDto.builder()
                .code(team.getCode())
                .build();
    }

    //팀 팀원 강퇴
    public Boolean deleteTeamMember(Long teamId, Long memberId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (user.getMember().getETeamRole().equals(ETeamRole.MEMBER)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED_ERROR);
        }

        Team team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
        Member member = membersRepository.findByIdAndTeam(memberId, team)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        membersRepository.delete(member);

        return true;
    }

    //팀 나가기
    public Boolean deleteTeam(Long teamId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Team team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        Member member = membersRepository.findByUserAndTeam(user, team)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        membersRepository.delete(member);

        return true;
    }

    //팀 생성
    public CreateTeamResponseDto createTeam(CreateTeamRequestDto createTeamRequestDto, UUID userId) {
        if (teamsRepository.existsByName(createTeamRequestDto.name())) {
            throw new CustomException(ErrorCode.CONFLICT_TEAM_NAME);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (membersRepository.existsByUser(user)) {
            throw new CustomException(ErrorCode.CONFLICT_TEAM_BUILDING);
        }

        Team team = Team.toTeamEntity(createTeamRequestDto.name(), codeGenerator());
        teamsRepository.save(team);

        Member member = Member.toMemberEntity(ETeamRole.LEADER, user, team);
        membersRepository.save(member);

        return CreateTeamResponseDto.builder()
                .name(team.getName())
                .code(team.getCode())
                .build();
    }

    //팀 참여
    public Boolean joinTeam(Long teamId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Team team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));

        if (membersRepository.countByTeam(team) > 4) {
            throw new CustomException(ErrorCode.CONFLICT_TEAM_COUNT);
        }

        Member member = Member.toMemberEntity(ETeamRole.MEMBER, user, team);
        membersRepository.save(member);

        return true;
    }

    //팀 코드 생성기
    private String codeGenerator() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }


}
