package com.money.app.module.tx.dto;

import com.money.app.util.common.enumtype.AbcType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DecisionRequsetDto {
    private LocalDate txDate;
    private Long amount;
    private Long categoryId;
    private String content;
    private String memo;
}