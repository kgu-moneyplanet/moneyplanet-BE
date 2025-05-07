package com.money.app.module.category.domain;

import com.money.app.module.stat.dailystat.domain.DailyStat;
import com.money.app.module.stat.monthlystat.domain.MonthlyStat;
import com.money.app.module.stat.weeklystat.domain.WeeklyStat;
import com.money.app.module.tx.domain.Tx;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter        // getName 등 get 메소드 자동생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // 파라미터 없는 기본 생성자 생성(JPA용)
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // 파라미터 있는 기본 생성자 생성(Builder용) - 외부호출 방지
@Builder      // 빌더 패턴 자동 생성
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 숫자 증가
    private Long id;

    @Column(length = 26, nullable = false, unique = true)
    private String name;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Tx> txList = new ArrayList<>(); // List 초기화

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DailyStat> dailyStatList = new ArrayList<>(); // List 초기화

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WeeklyStat> weeklyStatList = new ArrayList<>(); // List 초기화

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MonthlyStat> monthlyStatList = new ArrayList<>(); // List 초기화

    @PrePersist  //Insert 직전 db(JPA)에서 아래의 값을 수정
    public void onCreate() {
        this.createDatetime = LocalDateTime.now();
        this.updateDatetime = LocalDateTime.now();
    }

    @PreUpdate  //Update 직전 db(JPA)에서 아래의 값을 수정
    public void onUpdate() {
        this.updateDatetime = LocalDateTime.now();
    }

    public static Category create(String name) {
        return Category.builder()
                .name(name)
                .build();
    }
    public void update(String name) {
        this.name = name;
    }
}
