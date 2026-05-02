package tn.enicarthage.campushub.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.campushub.admin.model.Reservation;
import java.util.List;

@Repository
public interface AdminReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatut(Reservation.Statut statut);
    long countByStatut(Reservation.Statut statut);
}
