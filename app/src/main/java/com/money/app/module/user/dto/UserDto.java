package com.money.app.module.user.dto;
import com.money.app.module.user.domain.Planet;
import com.money.app.module.user.domain.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto { //내부 로직에 사용
    private String id;
    private String username;
    private String name;
    private String cellphone;
    private String email;
    private Planet planet;
    private String password;
    private int totalIncome;
    private int totalExpense;
    private LocalDate birth;
    private String gender;
    private String job;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
    private double target;
    private boolean achieved;
    private String prefer;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .cellphone(user.getCellphone())
                .email(user.getEmail())
                .planet(user.getPlanet())
                .totalIncome(user.getTotalIncome())
                .totalExpense(user.getTotalExpense())
                .birth(user.getBirth())
                .gender(user.getGender())
                .job(user.getJob())
                .createDatetime(user.getCreateDatetime())
                .updateDatetime(user.getUpdateDatetime())
                .target(user.getTarget())
                .build();
    }

}