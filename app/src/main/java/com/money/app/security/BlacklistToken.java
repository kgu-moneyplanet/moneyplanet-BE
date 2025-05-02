package com.money.app.security;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "BlacklistToken")
public class BlacklistToken {
    @Id
    private String token;  // 블랙리스트에 저장된 JWT 토큰
}
