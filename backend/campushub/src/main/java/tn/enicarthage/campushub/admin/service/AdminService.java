package tn.enicarthage.campushub.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.enicarthage.campushub.admin.dto.DashboardStatsDto;
import tn.enicarthage.campushub.model.enseignant.Reservation;
import tn.enicarthage.campushub.repository.enseignant.DemandeDocumentRepository;
import tn.enicarthage.campushub.repository.enseignant.EvenementRepository;
import tn.enicarthage.campushub.repository.enseignant.ReservationRepository;
import tn.enicarthage.campushub.repository.enseignant.SalleRepository;
import tn.enicarthage.campushub.model.enseignant.DemandeDocument;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;

import java.util.List;

/**
 * Service dédié au domaine Admin — Binôme 3
 * Responsabilités :
 * - Statistiques globales du dashboard
 * - Approbation / Rejet des réservations
 * - Gestion des utilisateurs (activation, suppression)
 * - Traitement des demandes de documents
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;
    private final EvenementRepository evenementRepository;
    private final DemandeDocumentRepository demandeDocumentRepository;

    // ==================== DASHBOARD ====================

    public DashboardStatsDto getDashboardStats() {
        log.info("📊 Génération des statistiques du dashboard admin");
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalUtilisateurs(userRepository.count());
        stats.setTotalEnseignants(userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.ENSEIGNANT).count());
        stats.setTotalEtudiants(userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.ETUDIANT).count());
        stats.setTotalSalles(salleRepository.count());
        stats.setSallesDisponibles(salleRepository.findByDisponibleTrue().size());
        stats.setTotalReservations(reservationRepository.count());
        stats.setReservationsEnAttente(reservationRepository.findByStatut(Reservation.Statut.EN_ATTENTE).size());
        stats.setReservationsApprouvees(reservationRepository.findByStatut(Reservation.Statut.APPROUVEE).size());
        stats.setTotalEvenements(evenementRepository.count());
        stats.setDemandesDocumentsEnAttente(
                demandeDocumentRepository.findByStatut(DemandeDocument.Statut.EN_ATTENTE).size());
        return stats;
    }

    // ==================== RÉSERVATIONS ====================

    public List<Reservation> getReservationsEnAttente() {
        log.info("📋 Admin : Récupération des réservations en attente");
        return reservationRepository.findByStatut(Reservation.Statut.EN_ATTENTE);
    }

    public Reservation approuverReservation(Long id, String commentaire) {
        log.info("✅ Admin : Approbation de la réservation ID: {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        reservation.setStatut(Reservation.Statut.APPROUVEE);
        reservation.setCommentaireAdmin(commentaire);
        return reservationRepository.save(reservation);
    }

    public Reservation rejeterReservation(Long id, String raison) {
        log.info("❌ Admin : Rejet de la réservation ID: {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        reservation.setStatut(Reservation.Statut.REJETEE);
        reservation.setCommentaireAdmin(raison);
        return reservationRepository.save(reservation);
    }

    // ==================== UTILISATEURS ====================

    public List<User> getAllUsers() {
        log.info("👥 Admin : Récupération de tous les utilisateurs");
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        log.info("🗑️ Admin : Suppression de l'utilisateur ID: {}", id);
        userRepository.deleteById(id);
    }
}
