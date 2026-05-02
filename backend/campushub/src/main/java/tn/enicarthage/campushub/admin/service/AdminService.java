package tn.enicarthage.campushub.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.enicarthage.campushub.admin.dto.AdminStatsDto;
import tn.enicarthage.campushub.admin.model.*;
import tn.enicarthage.campushub.admin.repository.*;
import tn.enicarthage.campushub.enseignant.repository.ReservationRepository;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;
import tn.enicarthage.campushub.student.repository.AdminRequestRepository;
import tn.enicarthage.campushub.student.repository.ClubRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final AdminEventRepository eventRepository;
    private final AdminReservationRepository adminReservationRepository;
    private final AdminClubRepository clubRepository;
    private final DemandeAdminRepository demandeAdminRepository;
    private final ReservationRepository reservationRepository;
    private final AdminRequestRepository adminRequestRepository;

    // ── STATS ──
    public AdminStatsDto getStats() {
        return new AdminStatsDto(
                userRepository.count(),
                eventRepository.count(),
                eventRepository.countByStatut(Event.Statut.EN_ATTENTE),
                reservationRepository.findByStatut(
                        tn.enicarthage.campushub.enseignant.model.Reservation.Statut.EN_ATTENTE).size(),
                adminRequestRepository.findByStatus(
                        tn.enicarthage.campushub.student.model.RequestStatus.PENDING).size(),
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
        return reservationRepository.findAll().stream()
                .map(r -> {
                    Reservation ar = new Reservation();
                    ar.setId(r.getId());
                    ar.setSalle(r.getSalle() != null ? r.getSalle().getNom() : "");
                    ar.setSalleNom(r.getSalle() != null ? r.getSalle().getNom() : "");
                    ar.setBatiment(r.getSalle() != null ? r.getSalle().getBatiment() : "");
                    ar.setDemandeur(r.getEnseignant() != null ?
                            r.getEnseignant().getPrenom() + " " + r.getEnseignant().getNom() : "");
                    ar.setMotif(r.getMotif());
                    ar.setDateDebut(r.getDateDebut());
                    ar.setDateFin(r.getDateFin());
                    ar.setStatut(Reservation.Statut.valueOf(r.getStatut().name()));
                    ar.setCommentaireAdmin(r.getCommentaireAdmin());
                    return ar;
                })
                .collect(Collectors.toList());
    }

    public Reservation approuverReservation(Long id) {
        tn.enicarthage.campushub.enseignant.model.Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        r.setStatut(tn.enicarthage.campushub.enseignant.model.Reservation.Statut.APPROUVEE);
        reservationRepository.save(r);
        Reservation ar = new Reservation();
        ar.setId(r.getId());
        ar.setSalle(r.getSalle() != null ? r.getSalle().getNom() : "");
        ar.setSalleNom(r.getSalle() != null ? r.getSalle().getNom() : "");
        ar.setBatiment(r.getSalle() != null ? r.getSalle().getBatiment() : "");
        ar.setDemandeur(r.getEnseignant() != null ?
                r.getEnseignant().getPrenom() + " " + r.getEnseignant().getNom() : "");
        ar.setMotif(r.getMotif());
        ar.setStatut(Reservation.Statut.APPROUVEE);
        return ar;
    }

    public Reservation rejeterReservation(Long id, String commentaire) {
        tn.enicarthage.campushub.enseignant.model.Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        r.setStatut(tn.enicarthage.campushub.enseignant.model.Reservation.Statut.REJETEE);
        r.setCommentaireAdmin(commentaire);
        reservationRepository.save(r);
        Reservation ar = new Reservation();
        ar.setId(r.getId());
        ar.setSalle(r.getSalle() != null ? r.getSalle().getNom() : "");
        ar.setStatut(Reservation.Statut.REJETEE);
        ar.setCommentaireAdmin(commentaire);
        return ar;
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

    // ── DEMANDES — lit depuis admin_requests (demandes étudiants) ──
    public List<DemandeAdmin> getAllDemandes() {
        return adminRequestRepository.findAll().stream()
                .map(r -> {
                    DemandeAdmin d = new DemandeAdmin();
                    d.setId(r.getId());

                    // Type — évite NullPointerException
                    d.setType(DemandeAdmin.Type.MATERIEL); // valeur par défaut
                    if (r.getType() != null) {
                        try {
                            String t = r.getType().name();
                            if (t.equals("CERTIFICATE")) d.setType(DemandeAdmin.Type.CERTIFICAT);
                            else if (t.equals("MATERIAL")) d.setType(DemandeAdmin.Type.MATERIEL);
                            else d.setType(DemandeAdmin.Type.MATERIEL);
                        } catch (Exception ignored) {}
                    }

                    // Detail
                    d.setDetail(r.getDescription() != null ? r.getDescription() : "");

                    // Demandeur
                    d.setDemandeur(r.getStudentId() != null ?
                            userRepository.findById(r.getStudentId())
                                    .map(u -> u.getPrenom() + " " + u.getNom())
                                    .orElse("Étudiant #" + r.getStudentId()) : "");

                    // Statut — mapping manuel
                    if (r.getStatus() == null) {
                        d.setStatut(DemandeAdmin.Statut.EN_ATTENTE);
                    } else {
                        switch (r.getStatus().name()) {
                            case "APPROVED": d.setStatut(DemandeAdmin.Statut.APPROUVE); break;
                            case "REJECTED": d.setStatut(DemandeAdmin.Statut.REJETE); break;
                            default: d.setStatut(DemandeAdmin.Statut.EN_ATTENTE); break;
                        }
                    }

                    d.setCreatedAt(r.getSubmittedAt());
                    d.setCommentaireAdmin(r.getAdminNote());
                    return d;
                })
                .collect(Collectors.toList());
    }
    public DemandeAdmin approuverDemande(Long id) {
        tn.enicarthage.campushub.student.model.AdminRequest r = adminRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found"));
        r.setStatus(tn.enicarthage.campushub.student.model.RequestStatus.APPROVED);
        adminRequestRepository.save(r);
        DemandeAdmin d = new DemandeAdmin();
        d.setId(r.getId());
        d.setStatut(DemandeAdmin.Statut.APPROUVE);
        d.setDetail(r.getDescription() != null ? r.getDescription() : "");
        d.setDemandeur("");
        d.setType(DemandeAdmin.Type.MATERIEL);
        return d;
    }

    public DemandeAdmin rejeterDemande(Long id, String commentaire) {
        tn.enicarthage.campushub.student.model.AdminRequest r = adminRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found"));
        r.setStatus(tn.enicarthage.campushub.student.model.RequestStatus.REJECTED);
        r.setAdminNote(commentaire);
        adminRequestRepository.save(r);
        DemandeAdmin d = new DemandeAdmin();
        d.setId(r.getId());
        d.setStatut(DemandeAdmin.Statut.REJETE);
        d.setCommentaireAdmin(commentaire);
        d.setDetail(r.getDescription() != null ? r.getDescription() : "");
        d.setDemandeur("");
        d.setType(DemandeAdmin.Type.MATERIEL);
        return d;
    }
    public String fixPasswords() {
        List<User> users = userRepository.findAll();
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder =
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        for (User user : users) {
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(encoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }
        return "✅ Mots de passe hashés avec succès";
    }
}