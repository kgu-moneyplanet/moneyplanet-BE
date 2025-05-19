package com.money.app.module.tx.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.money.app.module.user.domain.Planet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputSchema {
    @JsonProperty("user_id")
    private String userId;

    private String planet;
    private String gender;
    private String prefer;
    private int age;
    private String job;

    @JsonProperty("tx_date")
    private LocalDate txDate;

    private int amount;

    @JsonProperty("category_name")
    private String categoryName;

    private String content;
    private String memo;
}