package tn.enicarthage.campushub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private String id;
    private String salleId;
    private SalleDto salle;
    private String enseignantId;
    private UserDto enseignant;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String motif;
    private Integer nombreParticipants;
    private String statut;
    private String commentaireAdmin;
    private LocalDateTime createdAt;
}