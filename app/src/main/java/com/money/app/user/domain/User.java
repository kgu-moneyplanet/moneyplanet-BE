package com.money.app.user.domain;

import com.money.app.user.dto.UserCreateDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA용
@AllArgsConstructor(access = AccessLevel.PRIVATE) //Buider용
@Builder
@Table(name="User")
public class User {

    @Id
    @Column(length = 26, nullable = false)
    private String id;

    @Column(length = 26, nullable = false)
    private String name;

    @Column(length = 26)
    private String cellphone;

    @Column(length = 26, unique = true, nullable = false)
    private String email;

    @Column(length = 100, nullable = false) //비밀번호 해시화 후 저장
    private String password;

    @Column(length = 26, nullable = true)
    @Enumerated(EnumType.STRING)
    private Planet planet; //회원가입 전 NULL 혹은 DEFAULT

    private int totalIncome;

    private int totalExpense;

    private LocalDate birth;

    private String gender;

    @Column(length = 26)
    private String job;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;

    private int target;

    @Column(length = 255, nullable = true) //추후 수정
    private String prefer;

    @PrePersist
    public void onCreate() {
        this.createDatetime=LocalDateTime.now();
        this.updateDatetime=LocalDateTime.now();
        this.target=0;
        this.planet=null;
        this.totalIncome=0;
        this.totalExpense=0;
    }

    @PreUpdate
    public void onUpdate() {
        this.updateDatetime= LocalDateTime.now();
    }

    public static User create(UserCreateDto dto){
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .cellphone(dto.getCellphone())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .birth(dto.getBirth())
                .gender(dto.getGender())
                .job(dto.getJob())
                .prefer(null)
                .build();
    }

}
