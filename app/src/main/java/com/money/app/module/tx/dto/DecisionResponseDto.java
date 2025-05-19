package com.money.app.module.tx.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.money.app.util.common.enumtype.AbcType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DecisionResponseDto {
    @JsonProperty("abc")
    private AbcType abcType;
    private String reason;
    private String feedback;
}
