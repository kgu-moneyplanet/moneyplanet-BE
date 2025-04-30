package com.money.app.user.dto;

public record LoginResponseDto(
        String type,
        String token
) {}