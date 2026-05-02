package tn.enicarthage.campushub.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.campushub.student.model.EventRegistration;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository
        extends JpaRepository<EventRegistration, Long> {

    List<EventRegistration> findByStudentId(Long studentId);
    List<EventRegistration> findByEventId(Long eventId);
    Optional<EventRegistration> findByStudentIdAndEventId(
            Long studentId, Long eventId);
    boolean existsByStudentIdAndEventId(Long studentId, Long eventId);
}