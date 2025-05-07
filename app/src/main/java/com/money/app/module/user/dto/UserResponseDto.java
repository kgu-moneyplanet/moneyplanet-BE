package com.money.app.module.user.dto;

import com.money.app.module.user.domain.Planet;
import com.money.app.module.user.domain.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto { //외부 응답용(프론트용), Server->Client, 민감한 정보 제외
    private String username;
    private String name;
    private String cellphone;
    private String email;
    private Planet planet;
    private int totalIncome;
    private int totalExpense;
    private LocalDate birth;
    private String gender;
    private String job;
    private int target;
    private String prefer;


    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
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
                .target(user.getTarget())
                .build();
    }
}
