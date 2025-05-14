package com.money.app.module.tx.dto;

import com.money.app.util.common.enumtype.PlanetType;
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
    private String userId;
    private PlanetType planet;
    private String gender;
    private String prefer;
    private int age;
    private String job;
    private LocalDate txDate;
    private int amount;
    private String categoryName;
    private String content;
    private String memo;
}