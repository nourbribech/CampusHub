package tn.enicarthage.campushub.enseignant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.campushub.enseignant.model.Salle;

import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {

    List<Salle> findByBatiment(String batiment);

    List<Salle> findByDisponibleTrue();

    @Query("SELECT s FROM Salle s WHERE s.capacite >= :capacite")
    List<Salle> findByCapaciteGreaterThanEqual(@Param("capacite") Integer capacite);
}