package com.example.finalproject.domain.auth.dto.response;

import com.example.finalproject.domain.users.entity.Users;
import lombok.*;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private boolean socialLogin;
    private String provider;
    private UserSummary user;

    @Getter @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class UserSummary {
        private Long userId;
        private String role;

        public static UserSummary from(Users u) {
            return UserSummary.builder()
                    .userId(u.getId())
                    .role(u.getRole().name())
                    .build();
        }
    }
}
