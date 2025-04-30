package com.money.app.user.controller;

import com.money.app.security.JwtService;
import com.money.app.user.dto.LoginRequestDto;
import com.money.app.user.dto.LoginResponseDto;
import com.money.app.user.service.AuthService;
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

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(summary = "로그인", security = @SecurityRequirement(name = ""))
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

}
