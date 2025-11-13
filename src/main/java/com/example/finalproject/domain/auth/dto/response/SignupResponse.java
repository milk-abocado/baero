package com.example.finalproject.domain.auth.dto.response;

import com.example.finalproject.domain.users.entity.Users;
import lombok.*;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class SignupResponse {
    private Long id;
    private String email;
    private String nickname;

    public static SignupResponse from(Users u) {
        return SignupResponse.builder()
                .id(u.getId())
                .email(u.getEmail())
                .nickname(u.getNickname())
                .build();
    }
}
