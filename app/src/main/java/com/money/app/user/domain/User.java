package com.money.app.user.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(length = 26)
    private String id;

    @Column(length = 26)
    private String name;

    @Column(length = 26)
    private String cellphone;

    @Column(length = 26, unique = true)
    private String email;

    @Column(length = 26)
    private String password;

    @Nullable
    @Column(length = 26)
    private String planet; //회원가입 전 NULL 혹은 DEFAULT

    private int totalIncome;

    private int totalExpense;

    private LocalDate birth;

    private String gender;

    @Column(length = 26)
    private String job;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;

    private int target;

    @Column(length = 255) //추후 수정
    private String prefer;
}
