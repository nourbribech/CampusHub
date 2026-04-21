package tn.enicarthage.campushub.student.repository;
import tn.enicarthage.campushub.student.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.campushub.student.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatus(EventStatus status);
    List<Event> findByOrganizerId(Long organizerId);
}