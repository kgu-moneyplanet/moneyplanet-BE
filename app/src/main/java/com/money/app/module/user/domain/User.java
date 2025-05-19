package com.money.app.module.user.domain;

import com.money.app.module.stat.monthlystat.domain.MonthlyStat;
import com.money.app.module.stat.weeklystat.domain.WeeklyStat;
import com.money.app.module.stat.dailystat.domain.DailyStat;
import com.money.app.module.user.dto.UserCreateDto;
import com.money.app.util.common.enumtype.PlanetType;
import com.money.app.util.ulid.UlidUtil;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.ArrayList;
import com.money.app.module.tx.domain.Tx;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA용
@AllArgsConstructor(access = AccessLevel.PRIVATE) //Buider용
@Builder
@Table(name="User")
public class User {

    @Id
    @Column
    private String id; //PK, idex 조회용(ulid)

    @Column(length = 26, nullable = false, unique = true)
    private String username; //회원가입 시 유저에게 입력받는 id

    @Column(length = 26, nullable = false)
    private String name;

    @Column(length = 26)
    private String cellphone;

    @Column(length = 26, unique = true, nullable = false)
    private String email;

    @Column(length = 100, nullable = false) //비밀번호 해시화 후 저장
    private String password;

    private int totalIncome;

    private int totalExpense;

    private LocalDate birth;

    private String gender;

    @Column(length = 26)
    private String job;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;

    private double target;

    private boolean achieved;

    @Column(length = 255, nullable = true) //추후 수정
    private String prefer;

    @Enumerated(EnumType.STRING)
    private PlanetType planet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Tx> txList = new ArrayList<>(); // List 초기화

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DailyStat> dailyStatList = new ArrayList<>(); // List 초기화

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WeeklyStat> weeklyStatList = new ArrayList<>(); // List 초기화

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MonthlyStat> monthlyStatList = new ArrayList<>(); // List 초기화


    @PrePersist
    public void onCreate() {
        if(id==null) this.id= UlidUtil.generate();
        this.createDatetime=LocalDateTime.now();
        this.updateDatetime=LocalDateTime.now();
        this.target=0;
        this.totalIncome=0;
        this.totalExpense=0;
        this.achieved=false;
    }

    @PreUpdate
    public void onUpdate() {
        this.updateDatetime= LocalDateTime.now();
    }

    public static User create(UserCreateDto dto, String encodedPassword){
        return User.builder()
                .username(dto.getUsername())
                .name(dto.getName())
                .cellphone(dto.getCellphone())
                .email(dto.getEmail())
                .password(encodedPassword)
                .birth(dto.getBirth())
                .gender(dto.getGender())
                .job(dto.getJob())
                .prefer(dto.getPrefer())
                .planet(dto.getPlanet())
                .build();
    }
    public void update(String name, String cellphone, String email, LocalDate birth, String gender, String job, String prefer, PlanetType planet) {
        this.name=name;
        this.cellphone=cellphone;
        this.email=email;
        this.birth=birth;
        this.gender=gender;
        this.job=job;
        this.prefer=prefer;
        this.planet=planet;
    }

    public void increaseTargetByPercent(double percent) {
        this.target += percent;
        if (this.target >= 100) {
            // 목표 도달
            this.achieved = true;
        }
    }
}
