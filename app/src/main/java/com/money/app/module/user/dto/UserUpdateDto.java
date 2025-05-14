package com.money.app.module.user.dto;

import com.money.app.util.common.enumtype.PlanetType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {
    private String name;
    private String cellphone;
    private String email;
    private LocalDate birth;
    private String gender;
    private String job;
    private String prefer;
    private PlanetType planet;
}
