package com.money.app.util.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbcStatResponseDto {
    private Long totalA;
    private Long totalB;
    private Long totalC;
    private Long totalExpense;
}
