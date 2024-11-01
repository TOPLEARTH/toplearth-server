package com.gdsc.toplearth_server.infrastructure.repository.credit;

import com.gdsc.toplearth_server.domain.entity.credit.Credits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditsRepositoryImpl extends JpaRepository<Credits, Long> {
}
