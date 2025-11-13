package com.example.finalproject.domain.auth.dto.response;

import lombok.*;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class LogoutResponse {
    private String message;

    public static LogoutResponse success() {
        return new LogoutResponse("로그아웃이 완료되었습니다.");
    }
}
