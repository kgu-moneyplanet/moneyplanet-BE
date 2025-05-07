package com.money.app.module.user.controller;

import com.money.app.module.user.service.AuthService;
import com.money.app.module.user.dto.LoginRequestDto;
import com.money.app.module.user.dto.LoginResponseDto;
import com.money.app.util.response.ApiResponse;
import com.money.app.util.response.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인", security = @SecurityRequirement(name = ""))
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    @PostMapping("/log-out")  // url 오류뜨면 다시 오류안나는 방향으로 수정해주세요!
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        authService.logout(request);
        return ApiResponseUtil.success(HttpStatus.OK,"로그아웃 성공");
    }
}
