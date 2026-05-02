package tn.enicarthage.campushub.enseignant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.campushub.enseignant.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByEnseignantId(Long enseignantId);

    List<Reservation> findBySalleId(Long salleId);

    List<Reservation> findByStatut(Reservation.Statut statut);

    @Query("SELECT r FROM Reservation r WHERE r.salle.id = :salleId " +
            "AND ((r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut))")
    List<Reservation> findConflictingReservations(
            @Param("salleId") Long salleId,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin
    );

    @Query("SELECT r FROM Reservation r WHERE r.enseignant.id = :enseignantId " +
            "ORDER BY r.dateDebut DESC")
    List<Reservation> findByEnseignantIdOrderByDateDebutDesc(@Param("enseignantId") Long enseignantId);
}