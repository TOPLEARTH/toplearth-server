package com.gdsc.toplearth_server.application.service;

import com.gdsc.toplearth_server.application.dto.bootstrap.BootstrapResponseDto;
import com.gdsc.toplearth_server.application.dto.bootstrap.HomeInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.bootstrap.LegacyInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.bootstrap.TrashInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.matching.MatchingInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.mission.QuestDetailResponseDto;
import com.gdsc.toplearth_server.application.dto.mission.QuestInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingDetailResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingTeamInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.region.RegionRankInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.team.ReadTeamDistanceResponseDto;
import com.gdsc.toplearth_server.application.dto.team.ReadTeamLabelResponseDto;
import com.gdsc.toplearth_server.application.dto.team.ReadTeamResponseDto;
import com.gdsc.toplearth_server.application.dto.team.ReadTeamStatisticsResponseDto;
import com.gdsc.toplearth_server.application.dto.team.TeamMemberResponseDto;
import com.gdsc.toplearth_server.application.dto.user.UserInfoResponseDto;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.matching.Matching;
import com.gdsc.toplearth_server.domain.entity.mission.Mission;
import com.gdsc.toplearth_server.domain.entity.mission.type.EMissionType;
import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import com.gdsc.toplearth_server.domain.entity.plogging.type.ELabel;
import com.gdsc.toplearth_server.domain.entity.team.Member;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.infrastructure.repository.matching.MatchingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.mission.MissionRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingImagesRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingProjection;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.region.RegionRankProjection;
import com.gdsc.toplearth_server.infrastructure.repository.region.RegionRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.team.MemberRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BootstrapService {
    private final UserRepositoryImpl userRepositoryImpl;
    private final MissionRepositoryImpl missionRepositoryImpl;
    private final PloggingRepositoryImpl ploggingRepositoryImpl;
    private final PloggingImagesRepositoryImpl ploggingImagesRepositoryImpl;
    private final MatchingRepositoryImpl matchingRepositoryImpl;
    private final MemberRepositoryImpl memberRepositoryImpl;
    private final RegionRepositoryImpl regionRepositoryImpl;

    @Transactional
    public BootstrapResponseDto bootstrap(UUID userId) {
        System.err.println(userId);
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        HomeInfoResponseDto homeInfoResponseDto = getHomeInfo(user);

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.fromUserEntity(user);

        MatchingInfoResponseDto matchingInfoResponseDto = getMatchingInfo(user);

        List<Mission> dailyQuestList = missionRepositoryImpl.findByUserAndMissionType(user, EMissionType.DAILY);

        Map<String, List<QuestDetailResponseDto>> groupedDailyQuests = dailyQuestList.stream()
                .map(mission ->
                        switch (mission.getMissionName()) {
                            case PICKUP ->
                                    QuestDetailResponseDto.fromMissionEntity(null, mission.getTarget(), null, null,
                                            mission.getCurrent(), null, mission.getCreatedAt().toString(),
                                            mission.getCredit());
                            case FLOGGING -> QuestDetailResponseDto.fromMissionEntity(mission.getTarget(), null, null,
                                    mission.getCurrent(), null, null, mission.getCreatedAt().toString(),
                                    mission.getCredit());
                            case LABELING ->
                                    QuestDetailResponseDto.fromMissionEntity(null, null, mission.getTarget(), null,
                                            null, mission.getCurrent(), mission.getCreatedAt().toString(),
                                            mission.getCredit());
                        })
                .collect(Collectors.groupingBy(quest -> {
                    LocalDateTime createdDate = LocalDateTime.parse(quest.createdAt());
                    return createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }));

        QuestInfoResponseDto questInfoResponseDto = QuestInfoResponseDto.fromEntityList(groupedDailyQuests);

        ReadTeamResponseDto readTeamResponseDto = getReadTeam(user);

        Map<String, List<PloggingDetailResponseDto>> ploggingDetailDtoList = ploggingRepositoryImpl.findByUser(user)
                .stream()
                .map(plogging -> {
                    List<PloggingImage> ploggingImageList = ploggingImagesRepositoryImpl.findByPlogging(plogging);
                    PloggingTeamInfoResponseDto ploggingTeamInfo = getPloggingMatchingTeamInfo(userId);
                    return PloggingDetailResponseDto.fromPloggingEntity(
                            plogging,
                            ploggingImageList,
                            ploggingTeamInfo
                    );
                })
                .collect(Collectors.groupingBy(ploggingDetailResponseDto -> {
                    LocalDateTime createdDate = LocalDateTime.parse(ploggingDetailResponseDto.startedAt());
                    return createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }));

        PloggingInfoResponseDto ploggingInfoResponseDto = PloggingInfoResponseDto.fromPloggingDetailResponseDtoList(
                ploggingDetailDtoList);

        LegacyInfoResponseDto legacyInfoResponseDto = getLegacyInfo();

        List<RegionRankInfoResponseDto> regionRankInfoResponseDtos = getRegionRankInfo();

        return BootstrapResponseDto.of(
                userInfoResponseDto,
                homeInfoResponseDto,
                matchingInfoResponseDto,
                questInfoResponseDto,
                readTeamResponseDto,
                ploggingInfoResponseDto,
                legacyInfoResponseDto,
                regionRankInfoResponseDtos
        );
    }

    // 각각의 라벨링된 쓰레기 개수 조회
    public TrashInfoResponseDto getLabelCounts() {
        // 네이티브 쿼리로 조회한 값은 String 타입
        List<Object[]> labelCounts = ploggingImagesRepositoryImpl.countByELabel();
        Map<ELabel, Long> trashCountMap = new HashMap<>();

        for (Object[] row : labelCounts) {
            String labelString = (String) row[0];
            Long count = (Long) row[1];

            if (labelString == null || labelString.isBlank()) {
                labelString = String.valueOf(ELabel.UNKNOWN);
            }
            ELabel label = ELabel.valueOf(labelString.toUpperCase());
            trashCountMap.put(label, count);
        }

        return TrashInfoResponseDto.fromTrashCountMap(trashCountMap);
    }

    private HomeInfoResponseDto getHomeInfo(User user) {
        Integer daysBetween = Math.toIntExact(ChronoUnit.DAYS.between(user.getCreatedAt(), LocalDateTime.now()));

        PloggingProjection projection = ploggingRepositoryImpl.findByUserAndCreatedAt(user,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));

        Plogging plogging = ploggingRepositoryImpl.findByUserAndCreatedAtRecent(user);

        Integer recentPloggingDay = plogging.getStartedAt() == null ? -1 :
                Math.toIntExact(ChronoUnit.DAYS.between(plogging.getStartedAt(), LocalDateTime.now()));

        return HomeInfoResponseDto.of(recentPloggingDay, daysBetween, projection.getPloggingMonthlyCount(),
                projection.getPloggingMonthlyDuration().intValue(),
                projection.getBurnedCalories().intValue());
    }

    private LegacyInfoResponseDto getLegacyInfo() {
        Long totalUserCnt = userRepositoryImpl.count();
        Long totalTrashCnt = ploggingImagesRepositoryImpl.count();
        TrashInfoResponseDto trashInfo = getLabelCounts();
        return LegacyInfoResponseDto.of(totalUserCnt, totalTrashCnt, trashInfo);
    }

    private PloggingTeamInfoResponseDto getPloggingMatchingTeamInfo(UUID userId) {
        Optional<Team> team = ploggingRepositoryImpl.findTeamByUserId(userId);
        Optional<Team> opponentTeam = ploggingRepositoryImpl.findOpponentTeamByUserId(userId);

        if (team.isEmpty() || opponentTeam.isEmpty()) {
            return PloggingTeamInfoResponseDto.ofNull();
        }

        return PloggingTeamInfoResponseDto.fromPloggingTeamEntity(
                team.get().getId(),
                team.get().getName(),
                opponentTeam.get().getId(),
                opponentTeam.get().getName()
        );
    }

    private ReadTeamResponseDto getReadTeam(User user) {
        Team team = user.getMember() != null ? user.getMember().getTeam() : null;
        if (team == null) {
            return null; // 유저가 팀이 없으면 null 반환
        }

        Integer matchCnt = matchingRepositoryImpl.countByTeam(team); // 유저가 속한 팀의 매치 횟수
        Integer winCnt = matchingRepositoryImpl.countByTeamAndWinFlagIsTrue(team); // 유저가 속한 팀의 승리횟수

        List<Member> members = memberRepositoryImpl.findByTeam(team); // 그 유저가 속한 팀의 모든 멤버들을 조회한다. //멤버 정보에 사용할 예정

        List<TeamMemberResponseDto> memberResponseDtos = members.stream()
                .map(TeamMemberResponseDto::of)
                .toList();

        List<Plogging> ploggingList = ploggingRepositoryImpl.findByYearAndTeam(team.getCreatedAt().getYear(),
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

        return ReadTeamResponseDto.of(team, matchCnt, winCnt, memberResponseDtos, memberMonthlyDataMap);
    }

    private MatchingInfoResponseDto getMatchingInfo(User user) {
        Matching matching = matchingRepositoryImpl.findFirstByTeamOrderByStartedAtDesc(user.getMember().getTeam())
                .orElse(null);
        if (matching == null) {
            return null;
        }
        
        return MatchingInfoResponseDto.of(matching);
    }

    public List<RegionRankInfoResponseDto> getRegionRankInfo() {
        List<RegionRankProjection> projection = regionRepositoryImpl.findAllWithRank();

        return projection.stream()
                .map(regionRankProjection ->
                        RegionRankInfoResponseDto.of(
                                regionRankProjection.getId(),
                                regionRankProjection.getName(),
                                regionRankProjection.getTotalScore(),
                                regionRankProjection.getRank()
                        )
                )
                .collect(Collectors.toList());
    }


}
