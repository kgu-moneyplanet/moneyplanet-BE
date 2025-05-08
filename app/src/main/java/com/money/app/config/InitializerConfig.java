package com.money.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.money.app.module.category.service.CategoryService;

@Component
@RequiredArgsConstructor
public class InitializerConfig implements CommandLineRunner{
    private final CategoryService categoryService;

    @Override
    public void run(String... args) {
        categoryService.setupCategory();
    }
}