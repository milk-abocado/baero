package com.example.finalproject.domain.users.controller;

import com.example.finalproject.domain.users.service.AdminUsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminUsersService adminUsersService;

    @PutMapping("/{userId}/block")
    public ResponseEntity<String> blockUser(@PathVariable Long userId) {
        adminUsersService.blockUser(userId);
        return ResponseEntity.ok("User " + userId + " has been blocked by admin.");
    }

    @PutMapping("/{userId}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
        adminUsersService.unblockUser(userId);
        return ResponseEntity.ok("User " + userId + " has been unblocked by admin.");
    }
}
