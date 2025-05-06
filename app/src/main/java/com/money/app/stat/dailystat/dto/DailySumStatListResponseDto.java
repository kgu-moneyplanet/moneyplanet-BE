package com.money.app.stat.dailystat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailySumStatListResponseDto {
    private String weekday;
    private LocalDate day;
    private Long totalExpense;
    private Long totalIncome;
    private Long totalAmount;
}
