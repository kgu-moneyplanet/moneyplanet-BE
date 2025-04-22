package com.money.app.user.service;

import com.money.app.user.domain.User;
import com.money.app.user.dto.UserCreateDto;
import com.money.app.user.dto.UserDto;
import com.money.app.user.dto.UserResponseDto;
import com.money.app.user.dto.UserUpdateDto;
import com.money.app.user.exception.DuplicateFieldException;
import com.money.app.user.exception.UserNotFoundexception;
import com.money.app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkDuplicationCreate(String field, String value) {
        boolean isDuplicate=false;

        switch(field) {
            case "email":
                isDuplicate=userRepository.existsByEmail(value);
                break;
            case "id":
                isDuplicate=userRepository.existsById(value);
                break;
            case "cellphone":
                isDuplicate=userRepository.existsByCellphone(value);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 필드");
        }
        if(isDuplicate)
            throw new DuplicateFieldException(field, value+" 은 이미 사용중 입니다.");
    }
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundexception("사용자를 찾을 수 없습니다."));
        return UserResponseDto.fromEntity(user);
    }

    public UserResponseDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundexception("사용자를 찾을 수 없습니다."));
        return UserResponseDto.fromEntity(user);
    }

    public void createUser(UserCreateDto userCreateDto) {
        checkDuplicationCreate("id",userCreateDto.getId());
        checkDuplicationCreate("email", userCreateDto.getEmail());
        checkDuplicationCreate("cellphone",userCreateDto.getCellphone());
        User user=User.builder()
                .id(userCreateDto.getId())
                .name(userCreateDto.getName())
                .cellphone(userCreateDto.getCellphone())
                .email(userCreateDto.getEmail())
                .birth(userCreateDto.getBirth())
                .gender(userCreateDto.getGender())
                .job(userCreateDto.getJob())
                .prefer(null)
                .build();

       userRepository.save(user);
    }

    public void updateUser(String id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundexception("사용자를 찾을 수 없습니다."));
        checkDuplicationCreate("email", userUpdateDto.getEmail());
        checkDuplicationCreate("cellphone",userUpdateDto.getCellphone());
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
                .orElseThrow(() -> new UserNotFoundexception("사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }
}