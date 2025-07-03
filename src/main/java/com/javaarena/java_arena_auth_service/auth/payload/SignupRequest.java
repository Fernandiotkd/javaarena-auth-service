package com.javaarena.java_arena_auth_service.auth.payload;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private String roles;
}