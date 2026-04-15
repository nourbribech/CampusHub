package tn.enicarthage.campushub.service.enseignant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.campushub.model.enseignant.DemandeDocument;
import tn.enicarthage.campushub.repository.enseignant.DemandeDocumentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DemandeDocumentService {

    private final DemandeDocumentRepository demandeDocumentRepository;

    public List<DemandeDocument> getAllDemandes() {
        return demandeDocumentRepository.findAll();
    }

    public List<DemandeDocument> getDemandesByEtudiant(Long etudiantId) {
        return demandeDocumentRepository.findByDemandeurId(etudiantId);
    }

    public Optional<DemandeDocument> getDemandeById(Long id) {
        return demandeDocumentRepository.findById(id);
    }

    public DemandeDocument createDemande(DemandeDocument demande) {
        return demandeDocumentRepository.save(demande);
    }

    public DemandeDocument updateStatut(Long id, DemandeDocument.Statut statut, String commentaire) {
        DemandeDocument demande = demandeDocumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        demande.setStatut(statut);
        if (commentaire != null) {
            demande.setCommentaireAdmin(commentaire);
        }
        return demandeDocumentRepository.save(demande);
    }

    public void deleteDemande(Long id) {
        demandeDocumentRepository.deleteById(id);
    }

    public long countDemandes() {
        return demandeDocumentRepository.count();
    }
}
