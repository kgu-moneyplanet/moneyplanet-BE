package com.money.app.util.common.dto;

import com.money.app.util.common.enumtype.AbcType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AbcStatDto {
    private AbcType abc;
    private Long amount;
}