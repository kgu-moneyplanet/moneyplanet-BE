package com.money.app.tx.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
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

    @Column(length = 255)
    private String reason;

    @Column(length = 255)
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
}

