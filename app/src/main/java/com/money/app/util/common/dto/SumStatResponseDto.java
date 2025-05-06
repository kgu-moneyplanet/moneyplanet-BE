package com.money.app.util.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SumStatResponseDto {
    private Long totalExpense;
    private Long totalIncome;
    private Long totalAmount;
}
