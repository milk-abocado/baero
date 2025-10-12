package com.example.finalproject.domain.auth.dto.response;

import lombok.*;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
}