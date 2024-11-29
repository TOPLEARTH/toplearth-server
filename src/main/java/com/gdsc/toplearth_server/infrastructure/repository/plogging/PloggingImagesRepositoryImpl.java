package com.gdsc.toplearth_server.infrastructure.repository.plogging;

import com.gdsc.toplearth_server.domain.entity.plogging.Plogging;
import com.gdsc.toplearth_server.domain.entity.plogging.PloggingImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PloggingImagesRepositoryImpl extends JpaRepository<PloggingImage, Long> {
    List<PloggingImage> findByPlogging(Plogging plogging);

    // 네이티브 쿼리로 성능 최적화
    @Query(value = "SELECT e_label, COUNT(*) FROM plogging_image GROUP BY e_label", nativeQuery = true)
    List<Object[]> countByELabel();

    @Query(value = "SELECT e_label label, COUNT(*) labelCount FROM plogging_image GROUP BY e_label", nativeQuery = true)
    List<LabelProjection> countByELabelCount();

}
