package com.money.app.user.controller;

import com.money.app.user.dto.UserCreateDto;
import com.money.app.user.dto.UserDto;
import com.money.app.user.dto.UserResponseDto;
import com.money.app.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get/email")
    public ResponseEntity<UserResponseDto> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserCreateDto userCreateDto) {
        String msg=userService.createUser(userCreateDto);
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @RequestBody UserDto userDto) {

        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletedUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted User");
    }
}