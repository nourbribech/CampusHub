package tn.enicarthage.campushub.student.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.enicarthage.campushub.model.enseignant.DemandeDocument;
import tn.enicarthage.campushub.model.enseignant.Evenement;
import tn.enicarthage.campushub.repository.enseignant.DemandeDocumentRepository;
import tn.enicarthage.campushub.repository.enseignant.EvenementRepository;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service dédié au domaine Étudiant — Binôme 2
 * Responsabilités :
 * - Consulter et mettre à jour le profil étudiant
 * - Voir les événements disponibles
 * - Soumettre et suivre les demandes de documents administratifs
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final UserRepository userRepository;
    private final EvenementRepository evenementRepository;
    private final DemandeDocumentRepository demandeDocumentRepository;

    // ==================== PROFIL ====================

    public Optional<User> getStudentProfile(Long studentId) {
        log.info("👤 Récupération du profil étudiant ID: {}", studentId);
        return userRepository.findById(studentId);
    }

    public User updateStudentProfile(Long studentId, String avatar, String departement) {
        log.info("✏️ Mise à jour du profil étudiant ID: {}", studentId);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        if (avatar != null)
            student.setAvatar(avatar);
        if (departement != null)
            student.setDepartement(departement);
        return userRepository.save(student);
    }

    // ==================== ÉVÉNEMENTS ====================

    public List<Evenement> getEvenementsDisponibles() {
        log.info("📅 Récupération des événements disponibles pour les étudiants");
        return evenementRepository.findAll().stream()
                .filter(e -> e.getStatut() == Evenement.Statut.OUVERT)
                .toList();
    }

    // ==================== DEMANDES DOCUMENTS ====================

    public DemandeDocument soumettreDemandeDocument(Long studentId, DemandeDocument.TypeDocument type) {
        log.info("📄 Soumission d'une demande de document type {} pour l'étudiant {}", type, studentId);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        DemandeDocument demande = new DemandeDocument();
        demande.setDemandeur(student);
        demande.setTypeDocument(type);
        demande.setStatut(DemandeDocument.Statut.EN_ATTENTE);
        return demandeDocumentRepository.save(demande);
    }

    public List<DemandeDocument> getMesDemandes(Long studentId) {
        log.info("📋 Récupération des demandes de l'étudiant ID: {}", studentId);
        return demandeDocumentRepository.findByDemandeurId(studentId);
    }
}
