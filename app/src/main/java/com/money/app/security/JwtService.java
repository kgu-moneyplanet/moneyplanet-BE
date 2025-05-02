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
import java.util.Date;

@Component//bean주입
public class JwtService {

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;
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

    public boolean isTokenBlacklisted(String token) {
        return blacklistTokenRepository.existsByToken(token);  // 토큰이 블랙리스트에 있으면 true 반환
    }
}
