package com.gdsc.toplearth_server.application.service;

import com.gdsc.toplearth_server.application.dto.plogging.PloggingImageResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.UserPloggingFinishResponseDto;
import com.gdsc.toplearth_server.application.dto.plogging.UserPloggingStartResponseDto;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import com.gdsc.toplearth_server.domain.entity.plogging.type.ELabel;
import com.gdsc.toplearth_server.domain.entity.region.Region;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingImagesRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.region.RegionRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import com.gdsc.toplearth_server.presentation.request.plogging.CreatePloggingRequestDto;
import com.gdsc.toplearth_server.presentation.request.plogging.UpdatePloggingImageLabelRequestDto;
import com.gdsc.toplearth_server.presentation.request.plogging.UpdatePloggingRequestDto;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PloggingService {
    private final PloggingRepositoryImpl ploggingRepositoryImpl;
    private final PloggingImagesRepositoryImpl ploggingImagesRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;
    private final RegionRepositoryImpl regionRepositoryImpl;
    private final S3Service s3Service;

    public UserPloggingStartResponseDto createUserPlogging(
            UUID userId,
            CreatePloggingRequestDto createPloggingRequestDto
    ) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Region region = regionRepositoryImpl.findById(createPloggingRequestDto.regionId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REGION));
        Team team = user.getMember() != null ? user.getMember().getTeam() : null;

        Plogging plogging = Plogging.createUserPlogging(user, region, team, null);
        ploggingRepositoryImpl.save(plogging);

        return UserPloggingStartResponseDto.fromEntity(plogging);
    }

    public void createPloggingImage(
            UUID userId, Long ploggingId, MultipartFile ploggingImage,
            Double latitude, Double longitude
    ) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // DB 레벨에서 조회, 성능 향상
        Plogging plogging = ploggingRepositoryImpl.findByUserAndId(user, ploggingId);
//        Plogging plogging = user.getPlogging().stream()
//                .filter(p -> p.getId().equals(ploggingId))
//                .findFirst()
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PLOGGING));

        String ploggingImageUrl = s3Service.uploadPloggingImage(ploggingImage, plogging.getId());
        PloggingImage ploggingImageEntity = PloggingImage.createPloggingImage(
                plogging, ploggingImageUrl,
                latitude, longitude
        );

        ploggingImagesRepositoryImpl.save(ploggingImageEntity);
    }

    // 플로깅 종료
    public UserPloggingFinishResponseDto updatePlogging(
            UUID userId, Long ploggingId,
            MultipartFile ploggingProfileImage, UpdatePloggingRequestDto updatePloggingRequestDto
    ) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Plogging plogging = ploggingRepositoryImpl.findByUserAndId(user, ploggingId);

        List<PloggingImageResponseDto> ploggingImageResponseDtoList =
                ploggingImagesRepositoryImpl.findByPlogging(plogging).stream()
                        .map(PloggingImageResponseDto::fromPloggingImageEntity)
                        .collect(Collectors.toList());

        // s3에 플로깅 프로필 이미지 업로드
        String ploggingProfileImageUrl = s3Service.uploadPloggingImage(ploggingProfileImage, plogging.getId());

        // 플로깅 종료
        plogging.updatePlogging(
                updatePloggingRequestDto.distance(),
                updatePloggingRequestDto.pickUpCnt(),
                updatePloggingRequestDto.duration(),
                ploggingProfileImageUrl,
                updatePloggingRequestDto.burnedCalories()
        );

        // 지역 총 점수 업데이트
        Region region = plogging.getRegion();
        region.updateTotalScore(updatePloggingRequestDto.distance());

        return UserPloggingFinishResponseDto.fromEntityList(ploggingImageResponseDtoList);
    }

    // 플로깅 라벨링
    public void updatePloggingImageLabel(
            UUID userId, Long ploggingId, UpdatePloggingImageLabelRequestDto updatePloggingImageLabelRequestDto
    ) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Plogging plogging = ploggingRepositoryImpl.findByUserAndId(user, ploggingId);

        List<Long> ploggingImageIds = updatePloggingImageLabelRequestDto.ploggingImageId();
        List<String> labels = updatePloggingImageLabelRequestDto.label();

        if (ploggingImageIds.size() != labels.size()) {
            throw new CustomException(ErrorCode.NOT_MATCH_PLOGGING_IMAGE_LABEL);
        }

        // 이미지 ID와 라벨 매핑 후 업데이트
        IntStream.range(0, ploggingImageIds.size())
                .forEach(i -> {
                    Long imageId = ploggingImageIds.get(i);
                    String labelString = labels.get(i);

                    // 플로깅 이미지 조회
                    PloggingImage ploggingImage = ploggingImagesRepositoryImpl.findById(imageId)
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PLOGGING_IMAGE));

                    // 라벨 유효성 검증 및 업데이트
                    try {
                        ELabel label = ELabel.valueOf(labelString.toUpperCase());
                        ploggingImage.updateImageLabel(label);
                    } catch (IllegalArgumentException e) {
                        throw new CustomException(ErrorCode.INVALID_LABEL_TYPE);
                    }
                });
    }
}
