package tn.enicarthage.campushub.student.service;

import tn.enicarthage.campushub.enseignant.model.Evenement;
import tn.enicarthage.campushub.enseignant.repository.EvenementRepository;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;
import tn.enicarthage.campushub.student.model.EventRegistration;
import tn.enicarthage.campushub.student.repository.EventRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EvenementRepository evenementRepository;
    private final EventRegistrationRepository registrationRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Evenement> getApprovedEvents() {
        return evenementRepository.findByStatut(Evenement.Statut.OUVERT);
    }

    public Evenement getEvent(Long id) {
        return evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public String registerForEvent(Long eventId) {
        User user = getCurrentUser();
        Evenement event = getEvent(eventId);

        if (event.getStatut() != Evenement.Statut.OUVERT) {
            throw new RuntimeException("Event is not available");
        }

        if (registrationRepository.existsByStudentIdAndEventId(user.getId(), eventId)) {
            throw new RuntimeException("Already registered");
        }

        long registered = registrationRepository.findByEventId(eventId).size();
        if (registered >= event.getNbParticipantsMax()) {
            throw new RuntimeException("Event is full");
        }

        registrationRepository.save(EventRegistration.builder()
                .studentId(user.getId())
                .eventId(eventId)
                .build());

        return "Successfully registered for " + event.getTitre();
    }

    public String cancelRegistration(Long eventId) {
        User user = getCurrentUser();

        EventRegistration registration = registrationRepository
                .findByStudentIdAndEventId(user.getId(), eventId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registrationRepository.delete(registration);
        return "Registration cancelled";
    }

    public Evenement submitEvent(String titre, String description,
                                 String lieu, LocalDateTime dateDebut,
                                 LocalDateTime dateFin, int nbParticipantsMax) {
        User user = getCurrentUser();

        Evenement event = new Evenement();
        event.setTitre(titre);
        event.setDescription(description);
        event.setLieu(lieu);
        event.setDateDebut(dateDebut);
        event.setDateFin(dateFin);
        event.setNbParticipantsMax(nbParticipantsMax);
        event.setOrganisateur(user);
        event.setStatut(Evenement.Statut.EN_ATTENTE);

        return evenementRepository.save(event);
    }

    public List<Evenement> getMyRegistrations() {
        User user = getCurrentUser();
        return registrationRepository
                .findByStudentId(user.getId())
                .stream()
                .map(r -> getEvent(r.getEventId()))
                .toList();
    }
}