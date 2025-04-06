package com.restaurant.domain;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Student userInfo;
} 