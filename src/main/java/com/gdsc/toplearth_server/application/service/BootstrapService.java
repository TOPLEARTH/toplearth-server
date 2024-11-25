package com.gdsc.toplearth_server.application.service;

import com.gdsc.toplearth_server.application.dto.bootstrap.BootstrapResponseDto;
import com.gdsc.toplearth_server.application.dto.bootstrap.LegacyInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.bootstrap.TrashInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.mission.QuestDetailResponseDto;
import com.gdsc.toplearth_server.application.dto.mission.QuestInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingDetailResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingTeamInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.user.UserInfoResponseDto;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.mission.Mission;
import com.gdsc.toplearth_server.domain.entity.mission.type.EMissionType;
import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import com.gdsc.toplearth_server.domain.entity.plogging.type.ELabel;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.infrastructure.repository.mission.MissionRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingImagesRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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

    @Transactional(readOnly = true)
    public BootstrapResponseDto bootstrap(UUID userId) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.fromUserEntity(user);

        List<Mission> dailyQuestList = missionRepositoryImpl.findByUserAndMissionType(user, EMissionType.DAILY);

        List<QuestDetailResponseDto> dailyQuestDtoList = dailyQuestList.stream()
                .map(QuestDetailResponseDto::fromMissionEntity)
                .toList();

        QuestInfoResponseDto questInfoResponseDto = QuestInfoResponseDto.fromEntityList(
                dailyQuestDtoList
        );

        List<PloggingDetailResponseDto> ploggingDetailDtoList = ploggingRepositoryImpl.findByUser(user).stream()
                .map(plogging -> {
                    List<PloggingImage> ploggingImageList = ploggingImagesRepositoryImpl.findByPlogging(plogging);
                    PloggingTeamInfoResponseDto ploggingTeamInfo = getPloggingMatchingTeamInfo(userId);
                    return PloggingDetailResponseDto.fromPloggingEntity(
                            plogging,
                            ploggingImageList,
                            ploggingTeamInfo
                    );
                }).toList();

        PloggingInfoResponseDto ploggingInfoResponseDto = PloggingInfoResponseDto.fromPloggingDetailResponseDtoList(
                ploggingDetailDtoList);

        LegacyInfoResponseDto legacyInfoResponseDto = getLegacyInfo();

        return BootstrapResponseDto.of(
                userInfoResponseDto,
                questInfoResponseDto,
                ploggingInfoResponseDto,
                legacyInfoResponseDto
        );
    }

    // 각각의 라벨링된 쓰레기 개수 조회
    public TrashInfoResponseDto getLabelCounts() {
        List<Object[]> labelCounts = ploggingImagesRepositoryImpl.countByELabel();
        Map<ELabel, Long> trashCountMap = new HashMap<>();

        for (Object[] row : labelCounts) {
            ELabel label = ELabel.valueOf((String) row[0]); // 네이티브 쿼리로 조회한 값은 String 타입
            Long count = (Long) row[1];
            trashCountMap.put(label, count);
        }

        return TrashInfoResponseDto.fromTrashCountMap(trashCountMap);
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
                team.get().getId().toString(),
                team.get().getName(),
                opponentTeam.get().getId().toString(),
                opponentTeam.get().getName()
        );
    }
}
