package com.gdsc.toplearth_server.presentation.request.plogging;

import java.util.List;

public record UpdatePloggingImageLabelRequestDto(
        List<Long> ploggingImageId,  // 플로깅 이미지 ID
        List<String> label  // 플로깅 이미지 라벨
) {
}
