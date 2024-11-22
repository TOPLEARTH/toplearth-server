package com.gdsc.toplearth_server.application.dto.oauth;

import java.util.List;

public record ApplePublicKeyListDto(
        List<ApplePublicKeyDto> keys
) {
}
