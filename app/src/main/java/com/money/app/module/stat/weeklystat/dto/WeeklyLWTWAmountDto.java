package com.money.app.module.stat.weeklystat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyLWTWAmountDto {
    private String lastWeekCategoryName;
    private Long lastWeekCategoryAmount;
    private String thisWeekCategoryName;
    private Long thisWeekCategoryAmount;
    private Long diffAmount;

}
