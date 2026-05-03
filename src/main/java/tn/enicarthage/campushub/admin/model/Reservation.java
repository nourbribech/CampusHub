package tn.enicarthage.campushub.admin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity(name = "AdminReservation")
@Table(name = "admin_reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String salle;
    @Column
    private String batiment;
    @Column(name = "salle_nom")
    private String salleNom;

    @Column(nullable = false)
    private String demandeur;

    @Column(nullable = false)
    private String motif;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.EN_ATTENTE;

    @Column(name = "commentaire_admin")
    private String commentaireAdmin;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum Statut {
        EN_ATTENTE, APPROUVEE, REJETEE, ANNULEE
    }
}
