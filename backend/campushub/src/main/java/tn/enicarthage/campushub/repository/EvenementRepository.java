package tn.enicarthage.campushub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.campushub.model.Evenement;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByOrganisateurId(Long organisateurId);
    List<Evenement> findByStatut(Evenement.Statut statut);
    List<Evenement> findByDateDebutAfter(LocalDateTime date);
}
