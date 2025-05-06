package com.money.app.user.service;

import com.money.app.security.BlacklistToken;
import com.money.app.security.BlacklistTokenRepository;
import com.money.app.security.JwtService;
import com.money.app.user.dto.LoginRequestDto;
import com.money.app.user.dto.LoginResponseDto;
import com.money.app.util.exception.CustomException;
import com.money.app.util.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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

    public void logout(HttpServletRequest request) {
        // 1. 헤더에서 토큰 추출
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        }

        String token = header.substring(7); // "Bearer " 제거

        // 2. 토큰 유효성 검사
        if (!jwtService.validateToken(token)) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }

        // 3. 만료 시간 추출
        LocalDateTime expiration = jwtService.getExpiration(token);

        // 4. 블랙리스트 DB에 저장
        BlacklistToken blacklistedToken = new BlacklistToken(token, expiration);
        blacklistTokenRepository.save(blacklistedToken);
    }
}