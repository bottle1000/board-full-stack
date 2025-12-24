package com.boardfullstack.domain.user.service;

import com.boardfullstack.domain.user.dto.response.UserListResponse;
import com.boardfullstack.domain.user.entity.Status;
import com.boardfullstack.domain.user.entity.User;
import com.boardfullstack.domain.user.repository.UserRepository;
import com.boardfullstack.global.common.response.PagedResponse;
import com.boardfullstack.global.exception.CustomException;
import com.boardfullstack.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional
    public void banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getStatus() == Status.BANNED) {
            throw new CustomException(ErrorCode.USER_BANNED);
        }

        user.changeStatus(Status.BANNED);
    }

    // unban 기능 추가하기

    @Transactional(readOnly = true)
    public PagedResponse<UserListResponse> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        Page<UserListResponse> userResponse = users.map(UserListResponse::from);

        return PagedResponse.from(userResponse);
    }
}
