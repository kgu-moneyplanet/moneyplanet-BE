package com.money.app.module.user.dto;

public record LoginResponseDto(
        String type,
        String token
) {}