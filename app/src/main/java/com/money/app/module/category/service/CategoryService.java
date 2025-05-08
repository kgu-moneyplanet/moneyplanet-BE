package com.money.app.module.category.service;

import com.money.app.module.category.domain.Category;
import com.money.app.module.category.dto.CategoryResponseDto;
import com.money.app.module.category.dto.CategoryUpdateDto;
import com.money.app.module.category.repository.CategoryRepository;
import com.money.app.util.exception.CustomException;
import com.money.app.util.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void setupCategory() {
        if (categoryRepository.count() == 0){
            List<String> categoryList = List.of("식비","교통/차량","문화생활","마트/편의점","패션/미용",
                    "생활용품","주거/통신","건강","교육",
                    "경조사/회비","부모님","저축성 지출","세금","반려동물","기타");
            for (String name : categoryList){
                if(!categoryRepository.existsByName(name)){
                    categoryRepository.save(Category.create(name));
                }
            }
        }
    }

    public void createCategory(String name) {
        // 중복 검사
        if (categoryRepository.existsByName(name)) {
            throw new CustomException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = Category.create(name);

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
        }
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

    @Transactional(readOnly = true)
    public Category getCategoryEntityById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        return category;
    }
}
