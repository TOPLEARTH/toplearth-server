package com.gdsc.toplearth_server.infrastructure.repository.plogging;

import java.time.LocalDateTime;

public interface AdminPloggingProjection {
    Long getDuration();

    Long getTrashCount();

    LocalDateTime getStartAt();
}
