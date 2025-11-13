package com.example.finalproject.domain.auth.dto.response;

import lombok.*;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ForceLogoutResponse {
    private boolean terminated;
    private String message;

    public static ForceLogoutResponse success() {
        return new ForceLogoutResponse(true, "이전 세션이 종료되었습니다. 다시 로그인하세요.");
    }
}
