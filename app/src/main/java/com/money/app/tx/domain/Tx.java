package com.money.app.tx.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.AccessLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.money.app.category.domain.Category;
import com.money.app.user.domain.User;
import com.money.app.tx.dto.TxCreateDto;
import com.money.app.util.UlidUtil;

@Getter        // getName 등 get 메소드 자동생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // 파라미터 없는 기본 생성자 생성(JPA용)
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // 파라미터 있는 기본 생성자 생성(Builder용) - 외부호출 방지
@Builder      // 빌더 패턴 자동 생성
@Entity
@Table(name = "Tx")
public class Tx {
    @Id
    @Column(length = 26)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate txDate;

    @Column(length = 26, nullable = false)
    @Enumerated(EnumType.STRING)
    private TxType type;  // 예: EXPENSE, INCOME

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;


    @Column(length = 26)
    @Enumerated(EnumType.STRING)
    private AbcType abc;

    @Column(nullable = false)
    private int amount;

    @Column(length = 26)
    @Enumerated(EnumType.STRING)
    private MethodType method;  // 예: CARD, CASH 등

    @Column(length = 255)
    private String content;

    @Column(length = 255)
    private String memo;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;

    @OneToOne(mappedBy = "tx", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Report report;

    @PrePersist  //Insert 직전 db(JPA)에서 아래의 값을 수정
    public void onCreate() {
        this.createDatetime = LocalDateTime.now();
        this.updateDatetime = LocalDateTime.now();
    }

    @PreUpdate  //Update 직전 db(JPA)에서 아래의 값을 수정
    public void onUpdate() {
        this.updateDatetime = LocalDateTime.now();
    }

    public static Tx create(User user, Category category, TxCreateDto dto) {
        return Tx.builder()
                .id(UlidUtil.generate())
                .user(user)
                .txDate(dto.getTxDate())
                .type(dto.getType())
                .category(category)
                .abc(dto.getAbc())
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .content(dto.getContent())
                .memo(dto.getMemo())
                .build();
    }
}
