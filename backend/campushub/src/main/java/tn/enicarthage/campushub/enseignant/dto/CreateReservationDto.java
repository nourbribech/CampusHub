package tn.enicarthage.campushub.enseignant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationDto {
    private String salleId;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String motif;
    private Integer nombreParticipants;
}