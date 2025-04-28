package com.money.app.user.controller;

import com.money.app.security.JwtService;
import com.money.app.user.dto.LoginRequestDto;
import com.money.app.user.dto.LoginResponseDto;
import com.money.app.util.exception.CustomException;
import com.money.app.util.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(summary = "로그인", security = @SecurityRequirement(name = ""))
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        // 1) 이메일·비밀번호 인증 시도
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        // 2) 인증이 성공하면 auth.getName() 으로 “주체(subject)”를 꺼냄
        //    UserDetailsServiceImpl 에서 withUsername() 에 넣은 값, ID)
        String token = jwtService.getToken(auth.getName());
        // 3) 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        // expose 헤더도 그대로
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
        // 4) 바디에 JSON 형태로 토큰 정보 담기
        LoginResponseDto body = new LoginResponseDto("Bearer", token);
        // 5) 헤더 + 바디 함께 반환
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(body);
    }
}
