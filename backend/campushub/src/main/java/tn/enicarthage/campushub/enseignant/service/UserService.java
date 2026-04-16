package tn.enicarthage.campushub.enseignant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        log.info("📝 Création d'un nouvel utilisateur : {}", user.getEmail());
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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

    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == role)
                .toList();
    }

    public List<User> getUsersByDepartement(String departement) {
        return userRepository.findAll().stream()
                .filter(u -> departement.equals(u.getDepartement()))
                .toList();
    }

    public Map<User.Role, Long> getStatistiquesParRole() {
        return userRepository.findAll().stream()
                .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public long countUsers() {
        return userRepository.count();
    }
}