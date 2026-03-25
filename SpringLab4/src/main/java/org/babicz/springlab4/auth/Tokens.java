package org.babicz.springlab4.auth;
import lombok.Builder;

@Builder
public record Tokens(
        String accessToken,
        String refreshToken
) {}