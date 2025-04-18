package com.money.app.user.service;

import com.money.app.user.domain.User;
import com.money.app.user.dto.UserCreateDto;
import com.money.app.user.dto.UserDto;
import com.money.app.user.dto.UserResponseDto;
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

    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return UserResponseDto.fromEntity(user);
    }

    public UserResponseDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return UserResponseDto.fromEntity(user);
    }

    public String createUser(UserCreateDto userCreateDto) {
        userRepository.findByEmail(userCreateDto.getEmail()).ifPresent(user->{
            throw new RuntimeException("중복된 이메일입니다.");
        });
        userRepository.findByCellphone(userCreateDto.getCellphone()).ifPresent(user-> {
            throw new RuntimeException("중복된 핸드폰번호입니다.");
        });
        User user=User.builder()
                .id(userCreateDto.getId())
                .name(userCreateDto.getName())
                .cellphone(userCreateDto.getCellphone())
                .email(userCreateDto.getEmail())
                .planet()
                .totalIncome(0)
                .totalExpense(0)
                .birth(userCreateDto.getBirth())
                .gender(userCreateDto.getGender())
                .job(userCreateDto.getJob())
                .createDatetime(java.time.LocalDateTime.now())
                .updateDatetime(java.time.LocalDateTime.now())
                .target(0)
                .prefer()
                .build();

        userRepository.save(user);

        return ("회원가입이 완료되었습니다.");
    }

    public UserDto updateUser(String id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setCellphone(userDto.getCellphone());
        user.setEmail(userDto.getEmail());
        user.setBirth(userDto.getBirth());
        user.setGender(userDto.getGender());
        user.setJob(userDto.getJob());
        user.setUpdateDatetime(LocalDateTime.now());

        User updated=userRepository.save(user);

        return UserDto.fromEntity(updated);
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }
}