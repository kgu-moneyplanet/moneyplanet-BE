package com.money.app.user.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.ArrayList;
import com.money.app.tx.domain.Tx;

@Entity
@Getter
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
    private String nickname;

    @Column(length = 26)
    private String content;

    @Column(length = 26)
    private String cellphone;

    @Column(length = 26, unique = true)
    private String email;

    @Column(length = 26)
    private String password;

    @Column(length = 26)
    private String mbti;

    private int totalIncome;

    private int totalExpense;

    private LocalDate birth;

    private String gender;

    @Column(length = 26)
    private String job;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Tx> txList = new ArrayList<>(); // List 초기화
}

