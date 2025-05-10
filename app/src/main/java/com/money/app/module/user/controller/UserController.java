package com.money.app.module.user.controller;

import com.money.app.module.user.service.UserTargetCalculationService;
import com.money.app.security.CurrentUser;
import com.money.app.module.user.dto.UserCreateDto;
import com.money.app.module.user.dto.UserResponseDto;
import com.money.app.module.user.dto.UserUpdateDto;
import com.money.app.module.user.service.UserService;
import com.money.app.util.response.ApiResponse;
import com.money.app.util.response.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;
    @Autowired
    private UserTargetCalculationService userTargetCalculationService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createUser(@RequestBody UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
        return ApiResponseUtil.success(HttpStatus.CREATED, "회원 가입 성공");
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestBody UserUpdateDto userUpdateDto) {
        String id=currentUser.getUserId();
        userService.updateUser(id,userUpdateDto);
        return ApiResponseUtil.success(HttpStatus.OK, "회원 정보 수정 성공");
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deletedUser(@AuthenticationPrincipal CurrentUser currentUser, HttpServletRequest request) {
        String id=currentUser.getUserId();
        userService.deleteUser(id, request);
        return ApiResponseUtil.success(HttpStatus.OK,"회원 탈퇴 성공");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByUsername(@AuthenticationPrincipal CurrentUser currentUser) {
        String id=currentUser.getUserId();
        UserResponseDto user=userService.getUserById(id);
        return ApiResponseUtil.success(HttpStatus.OK,"회원 조회 성공", user);
    }

    @GetMapping("/manual-schedule-trigger") //수동 트리거
    public String triggerManualSchedule() {
        userTargetCalculationService.updateUserTargets(); // 스케줄링 메서드를 수동으로 호출
        return "스케줄링 작업이 수동으로 실행되었습니다.";
    }
}