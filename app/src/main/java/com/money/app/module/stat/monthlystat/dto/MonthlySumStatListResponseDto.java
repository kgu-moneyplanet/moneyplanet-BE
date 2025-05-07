package com.money.app.module.stat.monthlystat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySumStatListResponseDto {
    private int year;
    private int month;
    private Long totalExpense;
    private Long totalIncome;
    private Long totalAmount;
}
