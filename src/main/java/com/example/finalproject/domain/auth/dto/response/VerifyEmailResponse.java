package com.example.finalproject.domain.auth.dto.response;

import lombok.*;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class VerifyEmailResponse {
    private String verifiedToken;
    private int expiresIn;
}