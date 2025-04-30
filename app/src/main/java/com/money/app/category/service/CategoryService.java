package com.money.app.category.service;

import java.util.List;
import com.money.app.category.domain.Category;
import com.money.app.category.dto.CategoryDeleteDto;
import com.money.app.category.dto.CategoryResponseDto;
import com.money.app.category.dto.CategoryUpdateDto;
import com.money.app.category.repository.CategoryRepository;
import com.money.app.category.dto.CategoryCreateDto;
import com.money.app.util.exception.ErrorCode;
import com.money.app.util.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void createCategory(String name) {
        // 중복 검사
        if (categoryRepository.existsByName(name)) {
            throw new CustomException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        // 저장 Dto -> Entity로
        CategoryCreateDto dto = new CategoryCreateDto();
        dto.setName(name);
        Category category = Category.create(dto);

        categoryRepository.save(category);
    }
    @Transactional //트랜잭션 보장
    public void updateCategory(Long id, CategoryUpdateDto dto) {
        // ID로 기존 카테고리 조회
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 이름 중복 검사 (현재 카테고리 제외)
        if (categoryRepository.existsByName(dto.getName())
                && !category.getName().equals(dto.getName())) {
            throw new CustomException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        category.update(dto.getName());
    }

    public void deleteCategory(String name) {
        // ID로 기존 카테고리 조회
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()){
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        };
        return categories.stream()
                .map(category -> new CategoryResponseDto(category.getId(), category.getName()))
                .toList();
    }
    @Transactional(readOnly = true)

    public CategoryResponseDto getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)); // 값이 없으면 예외로 던짐
        return new CategoryResponseDto(category.getId(), category.getName());
    }
}
