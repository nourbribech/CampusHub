package tn.enicarthage.campushub.repository.enseignant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.enicarthage.campushub.model.enseignant.DemandeDocument;

import java.util.List;

@Repository
public interface DemandeDocumentRepository extends JpaRepository<DemandeDocument, Long> {
    List<DemandeDocument> findByDemandeurId(Long demandeurId);

    List<DemandeDocument> findByStatut(DemandeDocument.Statut statut);
}
