package com.money.app.stat.weeklystat.domain;

import com.money.app.category.domain.Category;
import com.money.app.user.domain.User;
import com.money.app.util.common.enumtype.AbcType;
import com.money.app.util.common.enumtype.TxType;
import com.money.app.util.ulid.UlidUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // 파라미터 없는 기본 생성자 생성(JPA용)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "WeeklyStat",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "userId", "year", "weekNum", "type", "categoryId", "abc"}))
public class WeeklyStat {

    @Id
    @Column(length = 26)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int weekNum;

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

    public static WeeklyStat create(User user, int year, int weekNum, TxType type, Category category, AbcType abc, Long amount) {
        return WeeklyStat.builder()
                .id(UlidUtil.generate())
                .user(user)
                .year(year)
                .weekNum(weekNum)
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
