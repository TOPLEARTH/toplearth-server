package com.gdsc.toplearth_server.application.service;

import com.gdsc.toplearth_server.application.dto.bootstrap.HomeInfoResponseDto;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.core.security.JwtDto;
import com.gdsc.toplearth_server.core.util.JwtUtil;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.domain.entity.user.type.ELoginProvider;
import com.gdsc.toplearth_server.domain.entity.user.type.EUserRole;
import com.gdsc.toplearth_server.infrastructure.repository.mission.MissionRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingImagesRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingProjection;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TestService {
    private final UserRepositoryImpl userRepository;
    private final JwtUtil jwtUtil;
    private final MissionRepositoryImpl missionRepositoryImpl;
    private final PloggingRepositoryImpl ploggingRepositoryImpl;
    private final PloggingImagesRepositoryImpl ploggingImagesRepositoryImpl;

    public JwtDto signIn(String username, EUserRole role) {
        User user = User.toUserEntity(username, "test", EUserRole.USER, ELoginProvider.NAVER, "1234");

        userRepository.save(user);

        log.info("userId" + user.getId());

        return jwtUtil.generateTokens(user.getId(), role);
    }

    public JwtDto signInAdmin(String username, EUserRole role) {
        User user = User.toUserEntity(username, "test", EUserRole.ADMIN, ELoginProvider.NAVER, "asdf");

        userRepository.save(user);

        log.info("userId" + user.getId());

        return jwtUtil.generateTokens(user.getId(), role);
    }


    public HomeInfoResponseDto getHomeInfo(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Integer daysBetween = Math.toIntExact(ChronoUnit.DAYS.between(LocalDateTime.now(), user.getCreatedAt()));

        PloggingProjection projection = ploggingRepositoryImpl.findByUserAndCreatedAt(user,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));

        return HomeInfoResponseDto.of(daysBetween, projection.getPloggingMonthlyCount(),
                projection.getPloggingMonthlyDuration().intValue());
    }

//    public PloggingInfoResponseDto getQuest(UUID userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
//
////        List<QuestDetailResponseDto> dailyQuestDtoList = dailyQuestList.stream()
////                .map(QuestDetailResponseDto::fromMissionEntity)
////                .toList();
//
//        Map<String, List<PloggingDetailResponseDto>> ploggingDetailDtoList = ploggingRepositoryImpl.findByUser(user)
//                .stream()
//                .map(plogging -> {
//                    List<PloggingImage> ploggingImageList = ploggingImagesRepositoryImpl.findByPlogging(plogging);
//                    PloggingTeamInfoResponseDto ploggingTeamInfo = getPloggingMatchingTeamInfo(userId);
//                    return PloggingDetailResponseDto.fromPloggingEntity(
//                            plogging,
//                            ploggingImageList,
//                            ploggingTeamInfo
//                    );
//                })
//                .collect(Collectors.groupingBy(ploggingDetailResponseDto -> {
//                    LocalDateTime createdDate = LocalDateTime.parse(ploggingDetailResponseDto.startedAt());
//                    return createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                }));
//
//        return PloggingInfoResponseDto.fromPloggingDetailResponseDtoList(
//                ploggingDetailDtoList
//        );
//    }
//
//    private PloggingTeamInfoResponseDto getPloggingMatchingTeamInfo(UUID userId) {
//        Optional<Team> team = ploggingRepositoryImpl.findTeamByUserId(userId);
//        Optional<Team> opponentTeam = ploggingRepositoryImpl.findOpponentTeamByUserId(userId);
//
//        if (team.isEmpty() || opponentTeam.isEmpty()) {
//            return PloggingTeamInfoResponseDto.ofNull();
//        }
//
//        return PloggingTeamInfoResponseDto.fromPloggingTeamEntity(
//                team.get().getId(),
//                team.get().getName(),
//                opponentTeam.get().getId(),
//                opponentTeam.get().getName()
//        );
//    }


}
