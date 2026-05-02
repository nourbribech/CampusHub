package tn.enicarthage.campushub.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private long totalUtilisateurs;
    private long totalEnseignants;
    private long totalEtudiants;
    private long totalSalles;
    private long sallesDisponibles;
    private long totalReservations;
    private long reservationsEnAttente;
    private long reservationsApprouvees;
    private long totalEvenements;
    private long demandesDocumentsEnAttente;
}
