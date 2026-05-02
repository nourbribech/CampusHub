package tn.enicarthage.campushub.enseignant.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import tn.enicarthage.campushub.shared.model.User;

@Entity
@Table(name = "evenements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFin;

    private String lieu;

    private Integer nbParticipantsMax;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.EN_ATTENTE;  // default to pending

    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    private User organisateur;

    private String imageUrl;
    private String club;

    @Column(name = "commentaire_admin", length = 1000)
    private String commentaireAdmin;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Statut {
        EN_ATTENTE,  // pending approval
        OUVERT,      // approved and open
        PLEIN,       // full
        ANNULE,
        TERMINE,
        REJETE
    }
}