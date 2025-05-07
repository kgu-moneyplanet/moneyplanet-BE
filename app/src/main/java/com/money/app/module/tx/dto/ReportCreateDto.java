package com.money.app.module.tx.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.money.app.util.common.enumtype.AbcType;

@Getter
@Setter
@NoArgsConstructor
public class ReportCreateDto {
    private String txId;
    private AbcType abc;
    private String reason;
    private String feedback;
}