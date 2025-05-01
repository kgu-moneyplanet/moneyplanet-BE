package com.money.app.tx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.money.app.util.common.AbcType;
import com.money.app.util.common.MethodType;
import com.money.app.util.common.TxType;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TxResponseDto {
    private String id;
    private String userId;
    private LocalDate txDate;
    private TxType type;
    private Long categoryId;
    private AbcType abc;
    private Long amount;
    private MethodType method;
    private String content;
    private String memo;
    private ReportResponseDto reportResponseDto;
}