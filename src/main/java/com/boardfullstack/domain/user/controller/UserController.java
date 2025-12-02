package com.boardfullstack.domain.user.controller;

import com.boardfullstack.domain.user.dto.request.SignUpRequest;
import com.boardfullstack.domain.user.dto.response.SignUpResponse;
import com.boardfullstack.domain.user.service.UserService;
import com.boardfullstack.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ApiResponse<SignUpResponse> signUp (@Valid @RequestBody SignUpRequest request) {
        return ApiResponse.ok(userService.signUp(request));
    }
}
