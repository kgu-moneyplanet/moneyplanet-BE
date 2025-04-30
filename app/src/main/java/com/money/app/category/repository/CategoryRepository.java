package com.money.app.category.repository;

import java.util.Optional;
import com.money.app.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);   // 이름 중복 검사 메서드
    Optional<Category> findByName(String name);
    Optional<Category> findById(Long id);
}
