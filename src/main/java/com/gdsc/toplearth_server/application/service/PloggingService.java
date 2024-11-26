package com.gdsc.toplearth_server.application.service;

import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import com.gdsc.toplearth_server.domain.entity.region.Region;
import com.gdsc.toplearth_server.domain.entity.team.Team;
import com.gdsc.toplearth_server.domain.entity.user.User;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingImagesRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.plogging.PloggingRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.region.RegionRepositoryImpl;
import com.gdsc.toplearth_server.infrastructure.repository.user.UserRepositoryImpl;
import com.gdsc.toplearth_server.presentation.request.plogging.CreatePloggingRequestDto;
import com.gdsc.toplearth_server.presentation.request.plogging.UpdatePloggingRequestDto;
import java.util.UUID;
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

    public void createUserPlogging(UUID userId, CreatePloggingRequestDto createPloggingRequestDto) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Region region = regionRepositoryImpl.findById(createPloggingRequestDto.regionId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REGION));
        Team team = user.getMember() != null ? user.getMember().getTeam() : null;

        Plogging plogging = Plogging.createUserPlogging(user, region, team, null);
        ploggingRepositoryImpl.save(plogging);
    }

    public void createPloggingImage(
            UUID userId, Long ploggingId, MultipartFile ploggingImage,
            Double latitude, Double longitude
    ) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Plogging plogging = ploggingRepositoryImpl.findById(ploggingId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PLOGGING));
        String ploggingImageUrl = s3Service.uploadPloggingImage(ploggingImage, plogging.getId());
        PloggingImage ploggingImageEntity = PloggingImage.createPloggingImage(
                plogging, ploggingImageUrl,
                latitude, longitude
        );
        ploggingImagesRepositoryImpl.save(ploggingImageEntity);
    }

    public void updatePlogging(UUID userId, Long ploggingId, UpdatePloggingRequestDto updatePloggingRequestDto) {
        User user = userRepositoryImpl.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Plogging plogging = ploggingRepositoryImpl.findById(ploggingId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PLOGGING));

    }
}
