package com.money.app.module.category.controller;

import com.money.app.module.category.dto.CategoryResponseDto;
import com.money.app.module.category.dto.CategoryUpdateDto;
import com.money.app.module.category.service.CategoryService;
import com.money.app.util.response.ApiResponse;
import com.money.app.util.response.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> createCategory(@PathVariable String name) {
        categoryService.createCategory(name);
        return ApiResponseUtil.success(HttpStatus.CREATED, "카테고리 생성 성공");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateDto dto) {
        categoryService.updateCategory(id, dto);
        return ApiResponseUtil.success(HttpStatus.OK,"카테고리 수정 성공");
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String name) {
        categoryService.deleteCategory(name);
        return ApiResponseUtil.success(HttpStatus.OK,"카테고리 삭제 성공");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ApiResponseUtil.success(HttpStatus.OK, "카테고리 전체 출력 성공", categories);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryByName(@PathVariable String name) {
        CategoryResponseDto category = categoryService.getCategoryByName(name);
        return ApiResponseUtil.success(HttpStatus.OK, "카테고리 조회 성공", category);
    }

}
