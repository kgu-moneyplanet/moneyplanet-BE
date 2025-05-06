package com.money.app.util.common.dto;

import com.money.app.util.common.enumtype.TxType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SumStatDto {
    private TxType type;
    private Long sum;
}
