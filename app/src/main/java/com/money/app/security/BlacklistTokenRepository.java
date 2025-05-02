package com.money.app.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, String> {
    boolean existsByToken(String token);  // 토큰이 블랙리스트에 존재하는지 확인
    void deleteByToken(String token);  // 토큰을 블랙리스트에서 삭제
}