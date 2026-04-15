package tn.enicarthage.campushub.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DemandeDocumentDto {
    private String id;
    private String typeDocument;
    private String demandeurId;
    private String demandeurNom;
    private LocalDateTime dateDemande;
    private String statut;
    private String commentaireAdmin;
}
