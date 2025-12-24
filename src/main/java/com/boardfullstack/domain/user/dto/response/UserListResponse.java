package com.boardfullstack.domain.user.dto.response;

import com.boardfullstack.domain.user.entity.Role;
import com.boardfullstack.domain.user.entity.Status;
import com.boardfullstack.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserListResponse {

    private Long userId;
    private String email;
    private String name;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;

    public static UserListResponse from(User user) {
        return new UserListResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
