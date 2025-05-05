package com.money.app.user.controller;

import com.money.app.security.CurrentUser;
import com.money.app.user.dto.UserCreateDto;
import com.money.app.user.dto.UserDto;
import com.money.app.user.dto.UserResponseDto;
import com.money.app.user.dto.UserUpdateDto;
import com.money.app.user.service.UserService;
import com.money.app.util.response.ApiResponse;
import com.money.app.util.response.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createUser(@RequestBody UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
        return ApiResponseUtil.success(HttpStatus.CREATED, "회원 가입 성공");
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestBody UserUpdateDto userUpdateDto) {
        String id=currentUser.getUserId();
        userService.updateUser(id,userUpdateDto);
        return ApiResponseUtil.success(HttpStatus.OK, "회원 정보 수정 성공");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deletedUser(@AuthenticationPrincipal CurrentUser currentUser) {
        String id=currentUser.getUserId();
        userService.deleteUser(id);
        return ApiResponseUtil.success(HttpStatus.OK,"회원 탈퇴 성공");
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByUsername(@AuthenticationPrincipal CurrentUser currentUser) {
        String id=currentUser.getUserId();
        UserResponseDto user=userService.getUserByUsername(id);
        return ApiResponseUtil.success(HttpStatus.OK,"회원 조회 성공", user);
    }
}