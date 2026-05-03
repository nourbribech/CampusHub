package tn.enicarthage.campushub.enseignant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/profile")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<User> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateProfile(@RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (body.containsKey("prenom")) user.setPrenom(body.get("prenom"));
        if (body.containsKey("nom")) user.setNom(body.get("nom"));
        if (body.containsKey("email")) user.setEmail(body.get("email"));
        if (body.containsKey("telephone")) user.setTelephone(body.get("telephone")); // ← ajoute
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(body.get("current"), user.getPassword())) {
            return ResponseEntity.badRequest().body("Mot de passe actuel incorrect");
        }
        user.setPassword(passwordEncoder.encode(body.get("new")));
        userRepository.save(user);
        return ResponseEntity.ok("Mot de passe mis à jour avec succès");
    }

}