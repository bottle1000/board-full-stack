package com.boardfullstack.domain.user.service;

import com.boardfullstack.domain.user.dto.request.LoginRequest;
import com.boardfullstack.domain.user.dto.request.SignUpRequest;
import com.boardfullstack.domain.user.dto.response.LoginResponse;
import com.boardfullstack.domain.user.dto.response.SignUpResponse;
import com.boardfullstack.domain.user.entity.Status;
import com.boardfullstack.domain.user.entity.User;
import com.boardfullstack.domain.user.repository.UserRepository;
import com.boardfullstack.global.config.JwtProperties;
import com.boardfullstack.global.exception.CustomException;
import com.boardfullstack.global.exception.ErrorCode;
import com.boardfullstack.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;

    @Transactional
    public SignUpResponse signUp (SignUpRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.createUser(request.getEmail(), encodedPassword, request.getName());
        userRepository.save(user);

        return SignUpResponse.from(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_INVALID_CREDENTIAL, "이메일 또는 비밀번호가 올바르지 않습니다."));

        if (user.getStatus() == Status.BANNED) {
            throw new CustomException(ErrorCode.USER_BANNED, "정지된 계정입니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.AUTH_INVALID_CREDENTIAL, "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());

        return LoginResponse.of(accessToken, user);
    }
}
