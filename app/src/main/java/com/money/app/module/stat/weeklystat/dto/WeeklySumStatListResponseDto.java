package com.money.app.module.stat.weeklystat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeeklySumStatListResponseDto {
    private int year;
    private int month;
    private int week;
    private Long totalExpense;
    private Long totalIncome;
    private Long totalAmount;
}
