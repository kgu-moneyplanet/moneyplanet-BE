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

import java.time.LocalDate;
import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    //형식 검사 메소드(email, gender, cellphone)
    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(regex);
    }
    private boolean isValidGender(String gender) {
        return gender != null && (gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F"));
    }
    private boolean isValidCellphone(String cellphone) {
        String regex = "^01[016789]-?\\d{3,4}-?\\d{4}$";
        return cellphone != null && cellphone.matches(regex);
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
        //email, gender, cellphone 형식 검사
        if (!isValidEmail(userCreateDto.getEmail())) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
        if(!isValidGender(userCreateDto.getGender())) {
            throw new CustomException(ErrorCode.INVALID_GENDER_FORMAT);
        }
        if(!isValidCellphone(userCreateDto.getCellphone())) {
            throw new CustomException(ErrorCode.INVALID_CELLPHONE_FORMAT);
        }
        //birth 유효성 검사(현재보다 미래 시점 생일 유효x)
        if (userCreateDto.getBirth().isAfter(LocalDate.now())) {
            throw new CustomException(ErrorCode.INVALID_BIRTH_FORMAT);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userCreateDto.getPassword());

        //저장 Dto->Entity
        User user = User.create(userCreateDto, encodedPassword); // 암호화된 비밀번호 저장

        userRepository.save(user);
    }

    public void updateUser(String id, UserUpdateDto userUpdateDto) {
        //id(PK) 조회, cellphone, email 중복 검사
        User user = userRepository.findByUsername(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (userRepository.existsByCellphone(userUpdateDto.getCellphone())) {
            throw new CustomException(ErrorCode.USER_CELLPHONE_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(userUpdateDto.getEmail())) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }
        
        //email, gender, cellphone 형식 검사
        if (!isValidEmail(userUpdateDto.getEmail())) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
        if(!isValidGender(userUpdateDto.getGender())) {
            throw new CustomException(ErrorCode.INVALID_GENDER_FORMAT);
        }
        if(!isValidCellphone(userUpdateDto.getCellphone())) {
            throw new CustomException(ErrorCode.INVALID_CELLPHONE_FORMAT);
        }
        //birth 유효성 검사(현재보다 미래 시점 생일 유효x)
        if (userUpdateDto.getBirth().isAfter(LocalDate.now())) {
            throw new CustomException(ErrorCode.INVALID_BIRTH_FORMAT);
        }

        user.update(userUpdateDto.getName(), userUpdateDto.getCellphone(), userUpdateDto.getEmail(), userUpdateDto.getBirth(),
                userUpdateDto.getGender(), userUpdateDto.getJob(), userUpdateDto.getPrefer());

        userRepository.save(user);
    }

    public void deleteUser(String id) {
        User user = userRepository.findByUsername(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponseDto.fromEntity(user);
    }
}