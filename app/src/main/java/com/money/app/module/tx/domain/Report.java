package com.money.app.module.tx.domain;
import com.money.app.util.common.enumtype.AbcType;
import com.money.app.util.ulid.UlidUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter        // getName 등 get 메소드 자동생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // 파라미터 없는 기본 생성자 생성(JPA용)
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // 파라미터 있는 기본 생성자 생성(Builder용) - 외부호출 방지
@Builder      // 빌더 패턴 자동 생성
@Entity
@Table(name = "Report")
public class Report {
    @Id
    @Column(length = 26)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "txId", nullable = false)
    private Tx tx;

    @Column(length = 26)
    @Enumerated(EnumType.STRING)
    private AbcType abc;  // 예: CARD, CASH 등

    @Lob
    @Column
    private String reason;

    @Lob
    @Column
    private String feedback;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;

    @PrePersist  //Insert 직전 db(JPA)에서 아래의 값을 수정
    public void onCreate() {
        this.createDatetime = LocalDateTime.now();
        this.updateDatetime = LocalDateTime.now();
    }

    @PreUpdate  //Update 직전 db(JPA)에서 아래의 값을 수정
    public void onUpdate() {
        this.updateDatetime = LocalDateTime.now();
    }

    public static Report create(Tx tx, AbcType abc, String reason, String feedback){
        return Report.builder()
                .id(UlidUtil.generate())
                .tx(tx)
                .abc(abc)
                .reason(reason)
                .feedback(feedback)
                .build();
    }

    public void updateAbc(AbcType abc){
        this.abc = abc;
    }

    public void update(String reason, String feedback){
        this.reason = reason;
        this.feedback = feedback;
    }
}

