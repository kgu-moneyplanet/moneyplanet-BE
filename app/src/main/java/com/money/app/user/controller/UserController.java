package com.money.app.user.controller;

import com.money.app.user.dto.UserCreateDto;
import com.money.app.user.dto.UserDto;
import com.money.app.user.dto.UserResponseDto;
import com.money.app.user.dto.UserUpdateDto;
import com.money.app.user.exception.DuplicateFieldException;
import com.money.app.user.exception.UserNotFoundexception;
import com.money.app.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(userService.getUserByEmail(email)); //OK(200)
        } catch(UserNotFoundexception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //Not Found(404)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //서버 에러(500)
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id)); //OK(200)
        } catch(UserNotFoundexception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //Not Found(404)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //서버 에러(500)
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserCreateDto userCreateDto) {
        try {
            userService.createUser(userCreateDto);  // 서비스에서 중복 이메일 체크 및 사용자 생성
            return ResponseEntity.status(HttpStatus.CREATED).build();  // 회원가입 성공(201), 프론트에서 정보 다시 받길 원하는 경우 수정 필요
        } catch (DuplicateFieldException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());  // 중복 에러(409)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 서버 에러(500)
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @RequestBody UserUpdateDto userUpdateDto) {
        try {
            userService.updateUser(id,userUpdateDto);
            return ResponseEntity.ok().build(); // 회원 수정 성공(200), 프론트에서 정보 다시 받길 원하는 경우 수정 필요
        } catch(UserNotFoundexception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //Not Found(404)
        } catch(DuplicateFieldException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); //중복 에러(409)
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //서버 에러(500)
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletedUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build(); //회원 삭제 성공(200)
        } catch(UserNotFoundexception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //Not Found(404)
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //서버 에러(500)
        }
    }
}