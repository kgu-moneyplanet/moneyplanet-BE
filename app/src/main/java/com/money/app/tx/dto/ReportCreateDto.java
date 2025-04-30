package com.money.app.tx.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.money.app.tx.domain.AbcType;

@Getter
@Setter
@NoArgsConstructor
public class ReportCreateDto {
    private String txId;
    private AbcType abc;
    private String reason;
    private String feedback;
}