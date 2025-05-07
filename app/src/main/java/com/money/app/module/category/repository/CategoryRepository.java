package com.money.app.module.category.repository;

import com.money.app.module.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);   // 이름 중복 검사 메서드
    Optional<Category> findByName(String name);
    Optional<Category> findById(Long id);
}
