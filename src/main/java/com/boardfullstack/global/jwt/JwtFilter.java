package com.boardfullstack.global.jwt;


import com.boardfullstack.global.security.CustomDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String token = resolveToken(request);

        if(token != null) {
            try {
                // 3. 토큰 검증 (유효성 검사, 만료 여부, 서명 검증 등)
                if (jwtProvider.validationToken(token)) {
                    Claims claims = jwtProvider.parseClaims(token);

                    Long userId = Long.valueOf(claims.getSubject());

                    // DB 에서 유저 조회 후 UserDetails 반환
                    UserDetails userDetails = detailsService.loadUserById(userId);

                    // 인증 객체 생성
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 4. 토큰이 유효하다면, 사용자 정보를 꺼내서 SecurityContext 에 인증 정보 세팅
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                log.error("JWT 인증 실패 : {}", e.getMessage());
            }
        }
        // 5. 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 1. 요청 헤더에서 Authorization 헤더 추출 (Bearer 토큰)
        String authHeader = request.getHeader("Authorization");

        // 2. Token 에서 Bearer 부분만 제외하기
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
