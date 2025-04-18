package com.money.app.user.dto;

import com.money.app.user.domain.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto { //유저 생성용, Client->Server
    private String id;
    private String name;
    private String cellphone;
    private String email;
    private String planet;
    private int totalIncome;
    private int totalExpense;
    private LocalDate birth;
    private String gender;
    private String job;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
    private int target;
    private String prefer;
}
