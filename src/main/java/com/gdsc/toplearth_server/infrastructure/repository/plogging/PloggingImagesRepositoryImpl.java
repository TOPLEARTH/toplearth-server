package com.gdsc.toplearth_server.infrastructure.repository.plogging;

import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PloggingImagesRepositoryImpl extends JpaRepository<PloggingImage, Long> {
}
