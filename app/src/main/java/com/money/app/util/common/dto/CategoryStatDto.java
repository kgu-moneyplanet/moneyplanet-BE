package com.money.app.util.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatDto {
    private Long categoryId;
    private String categoryName;
    private Long amount;
}
