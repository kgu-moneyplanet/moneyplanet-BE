package com.money.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component  //스프링 빈 주입
@RequiredArgsConstructor  //JWT 인자를 받는 생성자를 롬복이 생성
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1) 요청 헤더에서 토큰 가져오기
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 2) 토큰이 있으면 JWT 파싱 시도
        if (token != null) {
            String userId = jwtService.getAuthUser(request);
            // 3) JWT가 유효해서 사용자 정보(id 등)를 꺼낼 수 있으면
            if (userId != null) {
                CurrentUser principal = new CurrentUser(userId);
                Authentication authentication = // principal: 사용자 식별 값 (id)
                        new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
                // 4) SecurityContext에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // 5) 다음 필터(또는 컨트롤러)로 요청 전달
        filterChain.doFilter(request, response);
    }
}
