package com.gdsc.toplearth_server.application.dto.oauth;

public record ApplePublicKeyDto(
        String kty,
        String kid,
        String use,
        String alg,
        String n,
        String e
) {
}
