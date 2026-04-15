package tn.enicarthage.campushub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.enicarthage.campushub.model.User;
import tn.enicarthage.campushub.repository.UserRepository;

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

    // ==================== CRUD BASIQUE ====================

    public User createUser(User user) {
        log.info("📝 Création d'un nouvel utilisateur : {}", user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("❌ Email déjà existant : {}", user.getEmail());
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        User savedUser = userRepository.save(user);
        log.info("✅ Utilisateur créé avec succès - ID: {}", savedUser.getId());
        return savedUser;
    }

    public List<User> getAllUsers() {
        log.info("📋 Récupération de tous les utilisateurs");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        log.info("🔍 Recherche de l'utilisateur avec ID: {}", id);
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        log.info("🔍 Recherche de l'utilisateur par email: {}", email);
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long id, User userDetails) {
        log.info("✏️ Mise à jour de l'utilisateur ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));

        user.setNom(userDetails.getNom());
        user.setPrenom(userDetails.getPrenom());
        user.setEmail(userDetails.getEmail());
        user.setDepartement(userDetails.getDepartement());
        user.setAvatar(userDetails.getAvatar());

        User updatedUser = userRepository.save(user);
        log.info("✅ Utilisateur mis à jour avec succès");
        return updatedUser;
    }

    public void deleteUser(Long id) {
        log.info("🗑️ Suppression de l'utilisateur ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + id);
        }

        userRepository.deleteById(id);
        log.info("✅ Utilisateur supprimé avec succès");
    }

    // ==================== MÉTHODES AVANCÉES ====================

    /**
     * Authentification avec vérification de sécurité
     */
    public Optional<User> authenticate(String email, String password) {
        log.info("🔐 Tentative d'authentification pour : {}", email);

        Optional<User> user = userRepository.findByEmailAndPassword(email, password);

        if (user.isPresent()) {
            log.info("✅ Authentification réussie pour : {}", email);
        } else {
            log.warn("❌ Échec d'authentification pour : {}", email);
        }

        return user;
    }

    /**
     * Mise à jour de l'email avec vérification d'unicité
     */
    public void updateEmailById(Long id, String newEmail) {
        log.info("📧 Mise à jour de l'email pour l'utilisateur ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (userRepository.existsByEmail(newEmail) && !user.getEmail().equals(newEmail)) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        user.setEmail(newEmail);
        userRepository.save(user);
        log.info("✅ Email mis à jour avec succès");
    }

    /**
     * Récupérer les utilisateurs par rôle
     */
    public List<User> getUsersByRole(User.Role role) {
        log.info("👥 Récupération des utilisateurs avec le rôle : {}", role);
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == role)
                .toList();
    }

    /**
     * Récupérer les utilisateurs par département
     */
    public List<User> getUsersByDepartement(String departement) {
        log.info("🏢 Récupération des utilisateurs du département : {}", departement);
        return userRepository.findAll().stream()
                .filter(u -> departement.equals(u.getDepartement()))
                .toList();
    }

    /**
     * Statistiques avancées : Répartition par rôle
     */
    public Map<User.Role, Long> getStatistiquesParRole() {
        log.info("📊 Génération des statistiques par rôle");
        return userRepository.findAll().stream()
                .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));
    }

    /**
     * Statistiques avancées : Répartition par département
     */
    public Map<String, Long> getStatistiquesParDepartement() {
        log.info("📊 Génération des statistiques par département");
        return userRepository.findAll().stream()
                .filter(u -> u.getDepartement() != null)
                .collect(Collectors.groupingBy(User::getDepartement, Collectors.counting()));
    }

    /**
     * Recherche avancée : Utilisateurs dont le nom contient une chaîne
     */
    public List<User> searchUsersByName(String query) {
        log.info("🔎 Recherche d'utilisateurs avec le critère : {}", query);
        String lowerQuery = query.toLowerCase();
        return userRepository.findAll().stream()
                .filter(u -> u.getNom().toLowerCase().contains(lowerQuery) ||
                        u.getPrenom().toLowerCase().contains(lowerQuery))
                .toList();
    }

    /**
     * Compter les utilisateurs
     */
    public long countUsers() {
        long count = userRepository.count();
        log.info("📈 Nombre total d'utilisateurs : {}", count);
        return count;
    }

    /**
     * Compter les utilisateurs par rôle
     */
    public long countUsersByRole(User.Role role) {
        long count = userRepository.findAll().stream()
                .filter(u -> u.getRole() == role)
                .count();
        log.info("📈 Nombre d'utilisateurs avec le rôle {} : {}", role, count);
        return count;
    }

    /**
     * Vérifier si un email existe
     */
    public boolean emailExists(String email) {
        boolean exists = userRepository.existsByEmail(email);
        log.info("🔍 Email {} existe : {}", email, exists);
        return exists;
    }

    /**
     * Obtenir les enseignants d'un département spécifique
     */
    public List<User> getEnseignantsByDepartement(String departement) {
        log.info("👨‍🏫 Récupération des enseignants du département : {}", departement);
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.ENSEIGNANT)
                .filter(u -> departement.equals(u.getDepartement()))
                .toList();
    }

    /**
     * Obtenir les utilisateurs récemment créés (dernières 24h)
     */
    public List<User> getRecentUsers() {
        log.info("🆕 Récupération des utilisateurs récents");
        return userRepository.findAll().stream()
                .filter(u -> u.getCreatedAt() != null)
                .filter(u -> u.getCreatedAt().isAfter(
                        java.time.LocalDateTime.now().minusDays(1)
                ))
                .toList();
    }

    /**
     * Batch update - Mettre à jour le département de plusieurs utilisateurs
     */
    @Transactional
    public void updateDepartementForMultipleUsers(List<Long> userIds, String newDepartement) {
        log.info("🔄 Mise à jour en batch du département pour {} utilisateurs", userIds.size());

        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + userId));
            user.setDepartement(newDepartement);
            userRepository.save(user);
        }

        log.info("✅ Département mis à jour pour {} utilisateurs", userIds.size());
    }

    /**
     * Désactiver un utilisateur (soft delete simulation)
     */
    public void deactivateUser(Long id) {
        log.info("⏸️ Désactivation de l'utilisateur ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // On pourrait ajouter un champ 'actif' dans l'entité
        // Pour l'instant, on modifie l'email pour indiquer la désactivation
        if (!user.getEmail().startsWith("DEACTIVATED_")) {
            user.setEmail("DEACTIVATED_" + user.getEmail());
            userRepository.save(user);
            log.info("✅ Utilisateur désactivé");
        }
    }
}