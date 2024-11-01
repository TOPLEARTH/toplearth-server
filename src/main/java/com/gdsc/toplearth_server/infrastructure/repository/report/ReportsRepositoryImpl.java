package com.gdsc.toplearth_server.infrastructure.repository.report;

import com.gdsc.toplearth_server.domain.entity.report.Reports;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportsRepositoryImpl extends JpaRepository<Reports, Long> {
}
