package tn.enicarthage.campushub.student.service;

import tn.enicarthage.campushub.student.repository.EventRegistrationRepository;
import tn.enicarthage.campushub.student.repository.EventRepository;
import tn.enicarthage.campushub.student.model.Student;
import tn.enicarthage.campushub.student.repository.StudentRepository;
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
    private final StudentRepository studentRepository;

    // Get current logged in student
    private Student getCurrentStudent() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // Browse all approved events
    public List<Event> getApprovedEvents() {
        return eventRepository.findByStatus(EventStatus.APPROVED);
    }

    // Get single event
    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    // Register for an event
    public String registerForEvent(Long eventId) {
        Student student = getCurrentStudent();
        Event event = getEvent(eventId);

        if (event.getStatus() != EventStatus.APPROVED) {
            throw new RuntimeException("Event is not available");
        }

        if (registrationRepository.existsByStudentIdAndEventId(
                student.getId(), eventId)) {
            throw new RuntimeException("Already registered");
        }

        long registered = registrationRepository
                .findByEventId(eventId).size();
        if (registered >= event.getCapacity()) {
            throw new RuntimeException("Event is full");
        }

        registrationRepository.save(EventRegistration.builder()
                .studentId(student.getId())
                .eventId(eventId)
                .build());

        return "Successfully registered for " + event.getTitle();
    }

    // Cancel registration
    public String cancelRegistration(Long eventId) {
        Student student = getCurrentStudent();

        EventRegistration registration = registrationRepository
                .findByStudentIdAndEventId(student.getId(), eventId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registrationRepository.delete(registration);
        return "Registration cancelled";
    }

    // Club head — submit event for approval
    public Event submitEvent(Event event) {
        Student student = getCurrentStudent();
        event.setOrganizerId(student.getId());
        event.setStatus(EventStatus.PENDING);
        return eventRepository.save(event);
    }

    // Get my registrations
    public List<Event> getMyRegistrations() {
        Student student = getCurrentStudent();
        return registrationRepository
                .findByStudentId(student.getId())
                .stream()
                .map(r -> getEvent(r.getEventId()))
                .toList();
    }
}