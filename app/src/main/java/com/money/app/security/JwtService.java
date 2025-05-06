package com.money.app.security;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component//bean주입
public class JwtService {

    @Autowired
    private static final long EXPIRATION_TIME = 86400000;
    private static final String PREFIX = "Bearer ";

    @Value("${JWT_SECRET}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        // Base64 디코딩 없이, UTF-8 바이트 배열 그대로 키로 사용
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    //JWT 토큰 생성
    public String getToken(String userId) {
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
    //JWT 토큰 검증
    public String getAuthUser(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(PREFIX)) {
            return io.jsonwebtoken.Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(header.substring(PREFIX.length()))
                    .getBody()
                    .getSubject();
        }
        return null;
    }
    // 1. 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            io.jsonwebtoken.Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // 토큰을 파싱하고 유효성 검사
            return true;
        } catch (Exception e) {
            return false;  // 토큰이 유효하지 않거나 파싱에 실패하면 false 반환
        }
    }

    // 2. 토큰의 만료 시간 추출
    public LocalDateTime getExpiration(String token) {
        try {
            Date expiration = io.jsonwebtoken.Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration(); // 만료 시간 가져오기

            return expiration.toInstant()
                    .atZone(ZoneId.systemDefault()) // LocalDateTime 변환
                    .toLocalDateTime();
        } catch (Exception e) {
            throw new RuntimeException("토큰의 만료 시간을 추출하는 데 실패했습니다.");
        }
    }
}
