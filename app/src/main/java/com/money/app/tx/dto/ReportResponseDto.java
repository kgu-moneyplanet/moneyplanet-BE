package com.money.app.tx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.money.app.tx.domain.AbcType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {
    private AbcType abc;
    private String reason;
    private String feedback;
}