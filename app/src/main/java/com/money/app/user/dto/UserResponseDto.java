package com.money.app.user.dto;

import com.money.app.user.domain.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto { //외부 응답용(프론트용), Server->Client, 민감한 정보 제외
    private String id;
    private String name;
    private String nickname;
    private String content;
    private String cellphone;
    private String email;
    private String planet;
    private int totalIncome;
    private int totalExpense;
    private LocalDate birth;
    private String gender;
    private String job;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
    private int target;
    private String prefer;

    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
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
