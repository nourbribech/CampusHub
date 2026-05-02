package tn.enicarthage.campushub.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.enicarthage.campushub.admin.dto.AdminStatsDto;
import tn.enicarthage.campushub.admin.model.*;
import tn.enicarthage.campushub.admin.repository.*;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final AdminReservationRepository reservationRepository;
    private final ClubRepository clubRepository;
    private final DemandeAdminRepository demandeAdminRepository;

    // ── STATS ──
    public AdminStatsDto getStats() {
        return new AdminStatsDto(
                userRepository.count(),
                eventRepository.count(),
                eventRepository.countByStatut(Event.Statut.EN_ATTENTE),
                reservationRepository.countByStatut(Reservation.Statut.EN_ATTENTE),
                demandeAdminRepository.countByStatut(DemandeAdmin.Statut.EN_ATTENTE),
                clubRepository.count()
        );
    }

    // ── USERS ──
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ── EVENTS ──
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getPendingEvents() {
        return eventRepository.findByStatut(Event.Statut.EN_ATTENTE);
    }

    public Event approuverEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatut(Event.Statut.APPROUVE);
        return eventRepository.save(event);
    }

    public Event rejeterEvent(Long id, String commentaire) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatut(Event.Statut.REJETE);
        event.setCommentaireAdmin(commentaire);
        return eventRepository.save(event);
    }

    // ── RESERVATIONS ──
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation approuverReservation(Long id) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        r.setStatut(Reservation.Statut.APPROUVEE);
        return reservationRepository.save(r);
    }

    public Reservation rejeterReservation(Long id, String commentaire) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        r.setStatut(Reservation.Statut.REJETEE);
        r.setCommentaireAdmin(commentaire);
        return reservationRepository.save(r);
    }

    // ── CLUBS ──
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club approuverClub(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club not found"));
        club.setStatut(Club.Statut.ACTIF);
        return clubRepository.save(club);
    }

    public Club suspendreClub(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club not found"));
        club.setStatut(Club.Statut.SUSPENDU);
        return clubRepository.save(club);
    }

    // ── DEMANDES ──
    public List<DemandeAdmin> getAllDemandes() {
        return demandeAdminRepository.findAll();
    }

    public DemandeAdmin approuverDemande(Long id) {
        DemandeAdmin d = demandeAdminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found"));
        d.setStatut(DemandeAdmin.Statut.APPROUVE);
        return demandeAdminRepository.save(d);
    }

    public DemandeAdmin rejeterDemande(Long id, String commentaire) {
        DemandeAdmin d = demandeAdminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found"));
        d.setStatut(DemandeAdmin.Statut.REJETE);
        d.setCommentaireAdmin(commentaire);
        return demandeAdminRepository.save(d);
    }
}