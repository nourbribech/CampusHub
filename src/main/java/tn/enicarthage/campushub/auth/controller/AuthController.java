package tn.enicarthage.campushub.auth.controller;


import tn.enicarthage.campushub.auth.dto.AuthResponse;
import tn.enicarthage.campushub.auth.dto.LoginRequest;
import tn.enicarthage.campushub.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(originPatterns = "http://localhost:*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}