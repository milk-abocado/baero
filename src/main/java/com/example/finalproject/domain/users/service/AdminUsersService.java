package com.example.finalproject.domain.users.service;

import com.example.finalproject.domain.auth.exception.AuthApiException;
import com.example.finalproject.domain.auth.exception.AuthErrorCode;
import com.example.finalproject.domain.users.entity.Users;
import com.example.finalproject.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUsersService {

    private final UsersRepository usersRepository;

    /**
     * 관리자 권한으로 특정 사용자를 차단 상태로 변경합니다.
     *
     * @param userId 차단할 사용자의 ID
     */
    @Transactional
    public void blockUser(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> AuthApiException.of(AuthErrorCode.ACCOUNT_NOT_FOUND));
        user.block();
    }

    /**
     * 관리자 권한으로 특정 사용자의 차단 상태를 해제합니다.
     *
     * @param userId 차단 해제할 사용자의 ID
     */
    @Transactional
    public void unblockUser(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> AuthApiException.of(AuthErrorCode.ACCOUNT_NOT_FOUND));
        user.unblock();
    }
}
