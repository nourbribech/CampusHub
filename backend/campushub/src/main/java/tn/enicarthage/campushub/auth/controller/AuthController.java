package tn.enicarthage.campushub.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.auth.dto.AuthResponse;
import tn.enicarthage.campushub.auth.dto.LoginRequest;
import tn.enicarthage.campushub.auth.dto.RegisterRequest;
import tn.enicarthage.campushub.auth.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        log.info("POST /api/v1/auth/login");
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        log.info("POST /api/v1/auth/register");
        return ResponseEntity.ok(authService.register(request));
    }
}
