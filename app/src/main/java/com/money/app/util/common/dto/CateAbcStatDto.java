package com.money.app.util.common.dto;


import com.money.app.util.common.enumtype.AbcType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CateAbcStatDto {
    private Long categoryId;
    private String categoryName;
    private AbcType abc;
    private Long amount;
}
