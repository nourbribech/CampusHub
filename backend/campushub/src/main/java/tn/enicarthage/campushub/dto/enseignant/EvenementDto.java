package tn.enicarthage.campushub.dto.enseignant;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EvenementDto {
    private String id;
    private String titre;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String lieu;
    private Integer nbParticipantsMax;
    private String statut;
    private String organisateurId;
    private String imageUrl;
    private LocalDateTime createdAt;
}
