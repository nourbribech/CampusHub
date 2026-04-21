package tn.enicarthage.campushub.shared.service;


import tn.enicarthage.campushub.shared.dto.UserDto;           // adapte si le package est différent
import tn.enicarthage.campushub.shared.model.User;

import tn.enicarthage.campushub.shared.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Méthode pour créer un utilisateur avec mot de passe hashé (pour tests / admin)
     */
    public User createUser(String email, String rawPassword, String nom, String prenom, User.Role role) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(rawPassword))   // Hashé avec BCrypt
                .nom(nom)
                .prenom(prenom)
                .role(role)
                .build();

        return userRepository.save(user);
    }

    /**
     * Méthode optionnelle : trouver un utilisateur par email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Tu peux ajouter d'autres méthodes plus tard (update, delete, getAll...)
}