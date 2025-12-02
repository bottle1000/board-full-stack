package com.boardfullstack.domain.user.dto.response;

import com.boardfullstack.domain.user.entity.Role;
import com.boardfullstack.domain.user.entity.Status;
import com.boardfullstack.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponse {

    private Long userId;
    private String email;
    private String name;
    private Role role;
    private Status status;

    public static SignUpResponse from(User user) {
        return new SignUpResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getStatus()
        );
    }
}
