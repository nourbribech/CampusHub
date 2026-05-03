package tn.enicarthage.campushub.student.service;

import tn.enicarthage.campushub.student.repository.EventRegistrationRepository;
import tn.enicarthage.campushub.student.repository.EventRepository;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;
import tn.enicarthage.campushub.student.model.EventStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.enicarthage.campushub.student.model.Event;
import tn.enicarthage.campushub.student.model.EventRegistration;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final UserRepository userRepository;  // ← remplace StudentRepository

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Event> getApprovedEvents() {
        return eventRepository.findByStatus(EventStatus.APPROVED);
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public String registerForEvent(Long eventId) {
        User user = getCurrentUser();
        Event event = getEvent(eventId);

        if (event.getStatus() != EventStatus.APPROVED) {
            throw new RuntimeException("Event is not available");
        }

        if (registrationRepository.existsByStudentIdAndEventId(user.getId(), eventId)) {
            throw new RuntimeException("Already registered");
        }

        long registered = registrationRepository.findByEventId(eventId).size();
        if (registered >= event.getCapacity()) {
            throw new RuntimeException("Event is full");
        }

        registrationRepository.save(EventRegistration.builder()
                .studentId(user.getId())
                .eventId(eventId)
                .build());

        return "Successfully registered for " + event.getTitle();
    }

    public String cancelRegistration(Long eventId) {
        User user = getCurrentUser();
        EventRegistration registration = registrationRepository
                .findByStudentIdAndEventId(user.getId(), eventId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        registrationRepository.delete(registration);
        return "Registration cancelled";
    }

    public Event submitEvent(Event event) {
        User user = getCurrentUser();
        event.setOrganizerId(user.getId());
        event.setStatus(EventStatus.PENDING);
        return eventRepository.save(event);
    }

    public List<Event> getMyRegistrations() {
        User user = getCurrentUser();
        return registrationRepository
                .findByStudentId(user.getId())
                .stream()
                .map(r -> getEvent(r.getEventId()))
                .toList();
    }
    public long getRegistrationCount(Long eventId) {
        return registrationRepository.findByEventId(eventId).size();
    }
}