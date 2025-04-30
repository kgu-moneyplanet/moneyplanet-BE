package com.money.app.tx.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.money.app.tx.domain.AbcType;
import com.money.app.tx.domain.MethodType;
import com.money.app.tx.domain.TxType;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TxCreateRequestDto {
    private LocalDate txDate;
    private TxType type;
    private Long categoryId;
    private AbcType abc;
    private Long amount;
    private MethodType method;
    private String content;
    private String memo;
    private String reason;
    private String feedback;
}