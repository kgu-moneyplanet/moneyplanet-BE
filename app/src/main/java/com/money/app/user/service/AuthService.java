package com.money.app.user.service;

import com.money.app.security.JwtService;
import com.money.app.user.dto.LoginRequestDto;
import com.money.app.user.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.money.app.security.BlacklistToken;
import com.money.app.security.BlacklistTokenRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BlacklistTokenRepository blacklistTokenRepository;

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto request) {
        // 1) 인증 시도
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.id(), request.password())
        );
        // 2) 인증이 성공하면 auth.getName() 으로 “주체(subject)”를 꺼냄
        //    UserDetailsServiceImpl 에서 withUsername() 에 넣은 값, ID)
        String token = jwtService.getToken(auth.getName());
        // 3) 응답 헤더 + 바디 구성
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
        // 4) 바디에 JSON 형태로 토큰 정보 담기
        LoginResponseDto body = new LoginResponseDto("Bearer", token);
        // 5) 헤더 + 바디 함께 반환
        return ResponseEntity.ok().headers(headers).body(body);
    }

    // 로그아웃 메서드
    public ResponseEntity<String> logout(String token) {
        // 블랙리스트에 JWT 토큰 추가
        BlacklistToken blacklistToken = new BlacklistToken();
        blacklistToken.setToken(token);
        blacklistTokenRepository.save(blacklistToken);  // 블랙리스트에 저장

        return ResponseEntity.ok("로그아웃 성공");
    }
}