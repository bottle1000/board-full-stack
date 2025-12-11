package com.boardfullstack.domain.user.dto.response;

import com.boardfullstack.domain.user.entity.Role;
import com.boardfullstack.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private Long userId;
    private String email;
    private String name;
    private Role role;

    public static LoginResponse of(String accessToken, User user) {
        return new LoginResponse(
                accessToken,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }
}
