package tn.enicarthage.campushub.enseignant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.campushub.enseignant.model.Evenement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByStatut(Evenement.Statut statut);
    long countByStatut(Evenement.Statut statut);
    List<Evenement> findByDateDebutAfter(LocalDateTime date);
    List<Evenement> findByOrganisateurId(Long organisateurId);
}