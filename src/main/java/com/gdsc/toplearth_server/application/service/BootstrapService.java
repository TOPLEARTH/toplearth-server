package com.gdsc.toplearth_server.application.service;

import com.gdsc.toplearth_server.application.dto.bootstrap.BootstrapResponseDto;
import com.gdsc.toplearth_server.application.dto.mission.QuestDetailResponseDto;
import com.gdsc.toplearth_server.application.dto.mission.QuestInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingDetailResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.PloggingInfoResponseDto;
import com.gdsc.toplearth_server.application.dto.user.UserInfoResponseDto;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.mission.Mission;
import com.gdsc.toplearth_server.domain.entity.mission.type.EMissionType;
import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.infrastructure.repository.mission.MissionRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingImagesRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import java.util.List;
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
                    return PloggingDetailResponseDto.fromPloggingEntity(plogging, ploggingImageList);
                }).toList();

        PloggingInfoResponseDto ploggingInfoResponseDto = PloggingInfoResponseDto.fromPloggingDetailResponseDtoList(
                ploggingDetailDtoList);

        return BootstrapResponseDto.of(
                userInfoResponseDto,
                questInfoResponseDto,
                ploggingInfoResponseDto
        );
    }
}
