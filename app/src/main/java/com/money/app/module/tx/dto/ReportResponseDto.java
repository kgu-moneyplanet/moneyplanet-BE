package com.money.app.module.tx.dto;

import com.money.app.util.common.enumtype.AbcType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {
    private AbcType abc;
    private String reason;
    private String feedback;
}