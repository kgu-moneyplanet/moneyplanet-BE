package com.money.app.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentUser {
    private String userId;

    @Override
    public String toString() {
        return userId;
    }
}
