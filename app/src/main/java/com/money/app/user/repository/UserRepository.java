package com.money.app.user.repository;
import com.money.app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);  // 이메일 중복 체크
    boolean existsById(String id);  // 아이디 중복 체크
    boolean existsByCellphone(String cellphone);  // 휴대전화번호 중복 체크
    Optional<User> findByEmail(String email); //유저 조회용
    Optional<User> findById(String id); //유저 조회용
}