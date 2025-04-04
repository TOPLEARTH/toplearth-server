package com.gdsc.toplearth_server.infrastructure.repository.report;

import com.gdsc.toplearth_server.domain.entity.report.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepositoryImpl extends JpaRepository<Report, Long> {
    Page<Report> findAll(Pageable pageable);
}
