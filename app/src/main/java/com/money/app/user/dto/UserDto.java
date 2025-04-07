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
public class UserDto {
    private String id;
    private String name;
    private String nickname;
    private String content;
    private String cellphone;
    private String email;
    private String mbti;
    private int totalIncome;
    private int totalExpense;
    private LocalDate birth;
    private String gender;
    private String job;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .content(user.getContent())
                .cellphone(user.getCellphone())
                .email(user.getEmail())
                .mbti(user.getMbti())
                .totalIncome(user.getTotalIncome())
                .totalExpense(user.getTotalExpense())
                .birth(user.getBirth())
                .gender(user.getGender())
                .job(user.getJob())
                .createDatetime(user.getCreateDatetime())
                .updateDatetime(user.getUpdateDatetime())
                .build();
    }
}