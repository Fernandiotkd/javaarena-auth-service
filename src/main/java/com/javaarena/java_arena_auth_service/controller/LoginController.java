package com.javaarena.java_arena_auth_service.controller;

import com.javaarena.java_arena_auth_service.auth.payload.JwtResponse;
import com.javaarena.java_arena_auth_service.auth.payload.SignupRequest;
import com.javaarena.java_arena_auth_service.auth.util.JwtUtils;
import com.javaarena.java_arena_auth_service.dto.LoginRequest;
import com.javaarena.java_arena_auth_service.dto.LoginResponse;
import com.javaarena.java_arena_auth_service.model.User;
import com.javaarena.java_arena_auth_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public LoginController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        log.info("Request received in /signin endpoint");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        Optional<User> foundUser = userRepository.findByUsername(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", foundUser.get().getId(),
                foundUser.get().getUsername(), foundUser.get().getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        log.info("Request received in /sign up endpoint");

        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new LoginResponse("Error: ¡El nombre de usuario ya está en uso!"));
        }

        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new LoginResponse("Error: ¡El correo electrónico ya está en uso!"));
        }

        User user = new User(null, signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getRoles() != null ? signUpRequest.getRoles() : "USER",
                LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok(new LoginResponse("¡Usuario registrado exitosamente!"));
    }
}
