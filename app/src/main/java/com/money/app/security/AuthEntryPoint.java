package com.money.app.security;

import com.money.app.util.exception.ErrorCode;
import com.money.app.util.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        ErrorCode errorCode = ErrorCode.LOGIN_FAILED;  // 디폴트 설정

        if (authException instanceof BadCredentialsException) {  //비번 틀릴경우
            errorCode = ErrorCode.PASSWORD_NOT_CORRECT;
        } else if (authException instanceof UsernameNotFoundException){ // 아이디가 없는 경우
            errorCode = ErrorCode.USER_ID_NOT_FOUND;
        } else if (authException instanceof AuthenticationCredentialsNotFoundException  //토큰이 없는 경우
                || authException.getMessage().contains("Full authentication is required")) {
            errorCode = ErrorCode.TOKEN_NOT_FOUND;
        } else if (authException instanceof CredentialsExpiredException) {  //토큰이 만료된 경우
            errorCode = ErrorCode.TOKEN_TIME_OUT;  // 예시 (원하는 ErrorCode로)
        }

        // ApiResponse 형태로 응답 작성
        ApiResponse<Void> apiResponse = new ApiResponse<>(
                errorCode.getStatus().value(),
                errorCode.getMessage(),
                null
        );

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

}
