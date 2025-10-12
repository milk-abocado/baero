package com.example.finalproject.domain.auth.controller;

import com.example.finalproject.domain.auth.dto.request.*;
import com.example.finalproject.domain.auth.dto.response.*;
import com.example.finalproject.domain.auth.exception.AuthApiException;
import com.example.finalproject.domain.auth.exception.AuthErrorCode;
import com.example.finalproject.domain.auth.service.AuthService;
import com.example.finalproject.domain.users.entity.Users;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest req) {
        return ResponseEntity.status(201).body(authService.signupDto(req));
    }

    @PostMapping("/signup/email/request")
    public ResponseEntity<Void> signupEmail(@Valid @RequestBody EmailRequest req) {
        authService.sendSignupEmail(req);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/signup/email/verify")
    public ResponseEntity<VerifyEmailResponse> verifyEmail(@Valid @RequestBody EmailVerifyRequest req) {
        return ResponseEntity.ok(authService.verifySignupEmailDto(req));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.loginDto(req));
    }

    // 토큰 재발급
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @CookieValue(value = "refresh_token", required = false) String refreshCookie,
            @RequestBody(required = false) TokenRefreshRequest body
    ) {
        String refreshToken = resolveRefreshToken(authorization, refreshCookie, body);
        if (refreshToken == null || refreshToken.isBlank()) {
            // 동일 포맷을 위해 전역 예외로 위임
            throw AuthApiException.of(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
        return ResponseEntity.ok(authService.refreshDto(refreshToken));
    }

    private String resolveRefreshToken(String authorization, String refreshCookie, TokenRefreshRequest body) {
        if (authorization != null && authorization.toLowerCase().startsWith("bearer "))
            return authorization.substring(7).trim();
        if (refreshCookie != null && !refreshCookie.isBlank())
            return refreshCookie.trim();
        if (body != null && body.getRefreshToken() != null && !body.getRefreshToken().isBlank())
            return body.getRefreshToken().trim();
        return null;
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(Authentication authentication) {
        if (authentication == null || authentication.getCredentials() == null) {
            throw AuthApiException.of(AuthErrorCode.INVALID_ACCESS_TOKEN);
        }
        String access = authentication.getCredentials().toString();
        Long userId = extractUid(authentication.getDetails());
        authService.logout(access, userId);
        return ResponseEntity.ok(LogoutResponse.success());
    }

    @SuppressWarnings("unchecked")
    private Long extractUid(Object details) {
        if (details instanceof Map<?, ?> map) {
            Object v = map.get("uid");
            if (v instanceof Number n) return n.longValue();
            if (v != null) return Long.valueOf(v.toString());
        }
        throw AuthApiException.of(AuthErrorCode.INVALID_ACCESS_TOKEN);
    }

    // 강제 로그아웃(토큰 없이)
    @PostMapping("/force-logout")
    public ResponseEntity<ForceLogoutResponse> forceLogout(@Valid @RequestBody ForceLogoutRequest req) {
        authService.forceLogoutWithoutToken(req);
        return ResponseEntity.ok(ForceLogoutResponse.success());
    }
}
