package tn.enicarthage.campushub.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.campushub.admin.model.Event;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatut(Event.Statut statut);
    long countByStatut(Event.Statut statut);
}