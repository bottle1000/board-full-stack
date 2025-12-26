package com.boardfullstack.domain.user.controller;

import com.boardfullstack.domain.user.dto.response.UserListResponse;
import com.boardfullstack.domain.user.service.AdminService;
import com.boardfullstack.global.common.response.ApiResponse;
import com.boardfullstack.global.common.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/users/{userId}/ban")
    public ApiResponse<Void> banUser(@PathVariable Long userId) {
        adminService.banUser(userId);

        return ApiResponse.ok(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/users/{userId}/unban")
    public ApiResponse<Void> unbanUser(@PathVariable Long userId) {
        adminService.unbanUser(userId);

        return ApiResponse.ok(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ApiResponse<PagedResponse<UserListResponse>> getUsers(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        PagedResponse<UserListResponse> users = adminService.getUsers(pageable);

        return ApiResponse.ok(users);
    }
}
