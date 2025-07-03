package com.javaarena.java_arena_auth_service.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String  password;
}
