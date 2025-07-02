package com.javaarena.java_arena_auth_service.controller;

import com.javaarena.java_arena_auth_service.dto.LoginRequest;
import com.javaarena.java_arena_auth_service.dto.LoginResponse;
import com.javaarena.java_arena_auth_service.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        log.error("Request received");
        LoginResponse response = new LoginResponse();
        response.setMessage(loginService.getUserName(loginRequest.getId()));
        return ResponseEntity.ok(response);
    }
}
