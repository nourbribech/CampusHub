package tn.enicarthage.campushub.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.enicarthage.campushub.auth.dto.AuthResponse;
import tn.enicarthage.campushub.auth.dto.LoginRequest;
import tn.enicarthage.campushub.auth.dto.RegisterRequest;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;

/**
 * Service d'authentification — Partagé par tous les domaines
 * TODO: Intégrer Spring Security + JWT pour remplacer l'authentification simpliste
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    public AuthResponse login(LoginRequest request) {
        log.info("🔐 Tentative de connexion : {}", request.getEmail());
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));
        log.info("✅ Connexion réussie pour : {}", request.getEmail());
        // TODO: Générer un vrai token JWT
        return new AuthResponse("dummy-jwt-token", user.getEmail(),
                user.getRole().name(), user.getNom(), user.getPrenom());
    }

    public AuthResponse register(RegisterRequest request) {
        log.info("📝 Inscription : {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Un compte existe déjà avec cet email");
        }
        User user = new User();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // TODO: BCrypt
        user.setRole(User.Role.valueOf(request.getRole()));
        user.setDepartement(request.getDepartement());
        User saved = userRepository.save(user);
        log.info("✅ Compte créé pour : {}", saved.getEmail());
        return new AuthResponse("dummy-jwt-token", saved.getEmail(),
                saved.getRole().name(), saved.getNom(), saved.getPrenom());
    }
}
