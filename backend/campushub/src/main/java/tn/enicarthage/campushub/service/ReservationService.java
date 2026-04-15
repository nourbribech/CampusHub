package tn.enicarthage.campushub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.enicarthage.campushub.model.Reservation;
import tn.enicarthage.campushub.model.Salle;
import tn.enicarthage.campushub.model.User;
import tn.enicarthage.campushub.repository.ReservationRepository;
import tn.enicarthage.campushub.repository.SalleRepository;
import tn.enicarthage.campushub.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SalleRepository salleRepository;
    private final UserRepository userRepository;

    // CREATE
    public Reservation createReservation(Reservation reservation) {
        log.info("Création d'une nouvelle réservation pour la salle ID: {}", reservation.getSalle().getId());

        // Vérifier les conflits
        List<Reservation> conflits = reservationRepository.findConflictingReservations(
                reservation.getSalle().getId(),
                reservation.getDateDebut(),
                reservation.getDateFin()
        );

        if (!conflits.isEmpty()) {
            log.error("Conflit détecté pour la réservation");
            throw new RuntimeException("La salle est déjà réservée pour ce créneau");
        }

        // Vérifier la capacité
        if (reservation.getNombreParticipants() != null &&
                reservation.getNombreParticipants() > reservation.getSalle().getCapacite()) {
            log.error("Nombre de participants supérieur à la capacité de la salle");
            throw new RuntimeException("Le nombre de participants dépasse la capacité de la salle");
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Réservation créée avec succès - ID: {}", savedReservation.getId());
        return savedReservation;
    }

    // READ
    public List<Reservation> getAllReservations() {
        log.info("Récupération de toutes les réservations");
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        log.info("Recherche de la réservation avec ID: {}", id);
        return reservationRepository.findById(id);
    }

    public List<Reservation> getReservationsByEnseignant(Long enseignantId) {
        log.info("Récupération des réservations de l'enseignant ID: {}", enseignantId);
        return reservationRepository.findByEnseignantIdOrderByDateDebutDesc(enseignantId);
    }

    public List<Reservation> getReservationsBySalle(Long salleId) {
        log.info("Récupération des réservations de la salle ID: {}", salleId);
        return reservationRepository.findBySalleId(salleId);
    }

    public List<Reservation> getReservationsByStatut(Reservation.Statut statut) {
        log.info("Récupération des réservations avec le statut : {}", statut);
        return reservationRepository.findByStatut(statut);
    }

    // UPDATE
    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        log.info("Mise à jour de la réservation ID: {}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));

        // Vérifier les conflits si les dates changent
        if (!reservation.getDateDebut().equals(reservationDetails.getDateDebut()) ||
                !reservation.getDateFin().equals(reservationDetails.getDateFin())) {

            List<Reservation> conflits = reservationRepository.findConflictingReservations(
                    reservation.getSalle().getId(),
                    reservationDetails.getDateDebut(),
                    reservationDetails.getDateFin()
            );

            // Exclure la réservation actuelle des conflits
            conflits.removeIf(r -> r.getId().equals(id));

            if (!conflits.isEmpty()) {
                throw new RuntimeException("Conflit de réservation détecté");
            }
        }

        reservation.setDateDebut(reservationDetails.getDateDebut());
        reservation.setDateFin(reservationDetails.getDateFin());
        reservation.setMotif(reservationDetails.getMotif());
        reservation.setNombreParticipants(reservationDetails.getNombreParticipants());

        Reservation updatedReservation = reservationRepository.save(reservation);
        log.info("Réservation mise à jour avec succès");
        return updatedReservation;
    }

    public Reservation approuverReservation(Long id, String commentaire) {
        log.info("Approbation de la réservation ID: {}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        reservation.setStatut(Reservation.Statut.APPROUVEE);
        reservation.setCommentaireAdmin(commentaire);

        Reservation updatedReservation = reservationRepository.save(reservation);
        log.info("Réservation approuvée avec succès");
        return updatedReservation;
    }

    public Reservation rejeterReservation(Long id, String raison) {
        log.info("Rejet de la réservation ID: {}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        reservation.setStatut(Reservation.Statut.REJETEE);
        reservation.setCommentaireAdmin(raison);

        Reservation updatedReservation = reservationRepository.save(reservation);
        log.info("Réservation rejetée avec succès");
        return updatedReservation;
    }

    // DELETE
    public void deleteReservation(Long id) {
        log.info("Suppression de la réservation ID: {}", id);

        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Réservation non trouvée avec l'ID: " + id);
        }

        reservationRepository.deleteById(id);
        log.info("Réservation supprimée avec succès");
    }

    // BUSINESS METHODS
    public List<Reservation> getReservationsEnAttente() {
        log.info("Récupération des réservations en attente");
        return reservationRepository.findByStatut(Reservation.Statut.EN_ATTENTE);
    }

    public List<Reservation> getReservationsApprouvees() {
        log.info("Récupération des réservations approuvées");
        return reservationRepository.findByStatut(Reservation.Statut.APPROUVEE);
    }

    public boolean hasConflict(Long salleId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        List<Reservation> conflits = reservationRepository.findConflictingReservations(
                salleId, dateDebut, dateFin
        );

        boolean hasConflict = !conflits.isEmpty();
        log.info("Vérification de conflit pour la salle {} : {}", salleId, hasConflict);
        return hasConflict;
    }

    public long countReservations() {
        long count = reservationRepository.count();
        log.info("Nombre total de réservations : {}", count);
        return count;
    }

    public long countReservationsByStatut(Reservation.Statut statut) {
        long count = reservationRepository.findByStatut(statut).size();
        log.info("Nombre de réservations avec le statut {} : {}", statut, count);
        return count;
    }
}