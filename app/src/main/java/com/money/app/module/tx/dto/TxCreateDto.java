package com.money.app.module.tx.dto;

import com.money.app.util.common.enumtype.AbcType;
import com.money.app.util.common.enumtype.MethodType;
import com.money.app.util.common.enumtype.TxType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TxCreateDto {
    private LocalDate txDate;
    private TxType type;
    private Long categoryId;
    private AbcType abc;
    private Long amount;
    private MethodType method;
    private String content;
    private String memo;
    private String feedback;
}