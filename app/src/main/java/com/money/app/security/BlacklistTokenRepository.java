package com.money.app.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {
    boolean existsByToken(String token);
}
