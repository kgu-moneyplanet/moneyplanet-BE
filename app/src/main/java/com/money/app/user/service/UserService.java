package com.money.app.user.service;

import com.money.app.user.domain.User;
import com.money.app.user.dto.UserCreateDto;
import com.money.app.user.dto.UserResponseDto;
import com.money.app.user.dto.UserUpdateDto;
import com.money.app.util.exception.*;
import com.money.app.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void createUser(UserCreateDto userCreateDto) {
        //username(id), cellphone, email 중복 검사
        if (userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new CustomException(ErrorCode.USER_ID_ALREADY_EXISTS);
        }
        if (userRepository.existsByCellphone(userCreateDto.getCellphone())) {
            throw new CustomException(ErrorCode.USER_CELLPHONE_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userCreateDto.getPassword());

        //저장 Dto->Entity
        User user = User.create(userCreateDto);
        user.setPassword(encodedPassword); // 암호화된 비밀번호 저장

        userRepository.save(user);
    }

    public void updateUser(String id, UserUpdateDto userUpdateDto) {
        //id(PK) 조회, cellphone, email 중복 검사
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (userRepository.existsByCellphone(userUpdateDto.getCellphone())) {
            throw new CustomException(ErrorCode.USER_CELLPHONE_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(userUpdateDto.getEmail())) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }

        user.setName(userUpdateDto.getName());
        user.setCellphone(userUpdateDto.getCellphone());
        user.setEmail(userUpdateDto.getEmail());
        user.setBirth(userUpdateDto.getBirth());
        user.setGender(userUpdateDto.getGender());
        user.setJob(userUpdateDto.getJob());
        user.setPrefer(userUpdateDto.getPrefer());

        userRepository.save(user);
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponseDto.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponseDto.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponseDto.fromEntity(user);
    }
}