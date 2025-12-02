package com.boardfullstack.global.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Configuration
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 요청 헤더에서 Authorization 헤더 추출 (Bearer 토큰)
        // String authorizationHeader = request.getHeader("Authorization");

        // 2. 토큰이 있는 경우 "Bearer " prefix 제거하고 실제 토큰만 추출

        // 3. 토큰 검증 (유효성 검사, 만료 여부, 서명 검증 등)

        // 4. 토큰이 유효하다면, 사용자 정보를 꺼내서 SecurityContext 에 인증 정보 세팅

        // 5. 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}
