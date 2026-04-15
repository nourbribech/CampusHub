package tn.enicarthage.campushub.repository.enseignant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.campushub.model.enseignant.Salle;

import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {

    List<Salle> findByBatiment(String batiment);

    List<Salle> findByDisponibleTrue();

    @Query("SELECT s FROM Salle s WHERE s.capacite >= :capacite")
    List<Salle> findByCapaciteGreaterThanEqual(@Param("capacite") Integer capacite);
}