package com.javaarena.java_arena_auth_service.auth.util;

import java.security.SecureRandom;
import java.util.Base64;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class SecretGenerator {

    public static String generateToken(){
        Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
