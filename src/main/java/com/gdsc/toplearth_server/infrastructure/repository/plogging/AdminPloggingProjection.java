package com.gdsc.toplearth_server.infrastructure.repository.plogging;

public interface AdminPloggingProjection {
    Long getDuration();

    Long getTrashCount();

    String getStartAt();
}
