package com.money.app.stat.dailystat.domain;

import com.money.app.category.domain.Category;
import com.money.app.user.domain.User;
import com.money.app.util.common.enumtype.AbcType;
import com.money.app.util.common.enumtype.TxType;
import com.money.app.util.ulid.UlidUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // 파라미터 없는 기본 생성자 생성(JPA용)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "DailyStat",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "userId", "statDate", "type", "categoryId", "abc"}))
public class DailyStat {

    @Id
    @Column(length = 26)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate statDate;

    @Enumerated(EnumType.STRING)
    private TxType type; // INCOME or EXPENSE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    @Enumerated(EnumType.STRING)
    private AbcType abc;

    @Column(nullable = false)
    private Long amount;

    private LocalDateTime updateDatetime;



    @PrePersist  //Insert 직전 db(JPA)에서 아래의 값을 수정
    public void onCreate() {
        this.updateDatetime = LocalDateTime.now();
    }

    @PreUpdate  //Update 직전 db(JPA)에서 아래의 값을 수정
    public void onUpdate() {
        this.updateDatetime = LocalDateTime.now();
    }

    public static DailyStat create(User user, LocalDate statDate, TxType type, Category category, AbcType abc, Long amount) {
        return DailyStat.builder()
                .id(UlidUtil.generate())
                .user(user)
                .statDate(statDate)
                .type(type)
                .category(category)
                .abc(abc)
                .amount(amount)
                .build();
    }

    public void update(Long amount){
        this.amount = amount;
    }
}
