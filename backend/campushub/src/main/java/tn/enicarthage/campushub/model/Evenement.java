package tn.enicarthage.campushub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Statut statut = Statut.OUVERT;

    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    private User organisateur;

    private String imageUrl;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Statut {
        OUVERT,
        PLEIN,
        ANNULE,
        TERMINE
    }
}
