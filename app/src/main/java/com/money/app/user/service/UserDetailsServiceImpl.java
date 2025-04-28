package com.money.app.user.service;

import com.money.app.user.domain.User;
import com.money.app.user.repository.UserRepository;
import com.money.app.util.exception.CustomException;
import com.money.app.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final 필드 자동주입
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) { //AuthenticationManager.authenticate() 내부에서 호출
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_EMAIL_NOT_FOUND));

        return org.springframework.security.core.userdetails.User //Spring Security용 UserDetails 객체 반환
                .withUsername(user.getId())
                .password(user.getPassword())
                .build();
    }
}