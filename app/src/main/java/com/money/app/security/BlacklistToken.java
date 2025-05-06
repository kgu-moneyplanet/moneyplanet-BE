package com.money.app.security;

import com.money.app.util.ulid.UlidUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistToken {

    @Id
    private String id;

    private String token;

    @Column(nullable = false)
    private LocalDateTime expiration;
    @PrePersist
    public void onCreate() {
        if (id == null) this.id = UlidUtil.generate();
    }
    public BlacklistToken(String token, LocalDateTime expiration) {
        this.token = token;
        this.expiration = expiration;
    }
}
