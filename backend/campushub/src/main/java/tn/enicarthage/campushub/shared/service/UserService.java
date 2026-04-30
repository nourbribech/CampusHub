package tn.enicarthage.campushub.shared.service;


import tn.enicarthage.campushub.shared.dto.UserDto;           // adapte si le package est différent
import tn.enicarthage.campushub.shared.model.User;

import tn.enicarthage.campushub.shared.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Méthode pour créer un utilisateur avec mot de passe hashé (pour tests / admin)
     */
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà : " + user.getEmail());
        }
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Méthode optionnelle : trouver un utilisateur par email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Tu peux ajouter d'autres méthodes plus tard (update, delete, getAll...)
    // In shared/service/UserService.java — add these methods:

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        user.setNom(userDetails.getNom());
        user.setPrenom(userDetails.getPrenom());
        user.setEmail(userDetails.getEmail());
        user.setDepartement(userDetails.getDepartement());
        user.setAvatar(userDetails.getAvatar());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == role)
                .toList();
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public long countUsers() {
        return userRepository.count();
    }

// DO NOT carry over the raw authenticate(email, password) method — it's unsafe
}