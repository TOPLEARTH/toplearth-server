package com.gdsc.toplearth_server.infrastructure.repository.credit;

import com.gdsc.toplearth_server.domain.entity.credit.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepositoryImpl extends JpaRepository<Credit, Long> {
}
