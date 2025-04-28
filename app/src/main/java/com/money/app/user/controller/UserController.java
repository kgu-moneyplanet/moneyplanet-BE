package com.money.app.user.controller;

import com.money.app.user.dto.UserCreateDto;
import com.money.app.user.dto.UserDto;
import com.money.app.user.dto.UserResponseDto;
import com.money.app.user.dto.UserUpdateDto;
import com.money.app.user.service.UserService;
import com.money.app.util.response.ApiResponse;
import com.money.app.util.response.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PatchMapping("/update/{ulid}")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @PathVariable String ulid,
            @RequestBody UserUpdateDto userUpdateDto) {
        userService.updateUser(ulid,userUpdateDto);
        return ApiResponseUtil.success(HttpStatus.OK, "회원 정보 수정 성공");
    }

    @DeleteMapping("/delete/{ulid}")
    public ResponseEntity<ApiResponse<Void>> deletedUser(@PathVariable String ulid) {
        userService.deleteUser(ulid);
        return ApiResponseUtil.success(HttpStatus.OK,"회원 탈퇴 성공");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByUlid(@PathVariable String ulid) {
        UserResponseDto user=userService.getUserByUlid(ulid);
        return ApiResponseUtil.success(HttpStatus.OK,"회원 조회 성공", user);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable String id) {
        UserResponseDto user=userService.getUserById(id);
        return ApiResponseUtil.success(HttpStatus.OK,"회원 조회 성공", user);
    }

    @GetMapping("/get/{email}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByEmail(@PathVariable String email) {
        UserResponseDto user=userService.getUserByEmail(email);
        return ApiResponseUtil.success(HttpStatus.OK,"회원 조회 성공", user);
    }
}