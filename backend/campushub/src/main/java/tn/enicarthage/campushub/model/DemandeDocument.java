package tn.enicarthage.campushub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDocument typeDocument;

    @ManyToOne
    @JoinColumn(name = "demandeur_id", nullable = false)
    private User demandeur;

    private LocalDateTime dateDemande = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.EN_ATTENTE;

    @Column(columnDefinition = "TEXT")
    private String commentaireAdmin;

    public enum TypeDocument {
        CERTIFICAT_SCOLARITE,
        RELEVE_NOTES,
        ATTESTATION_PRESENCE,
        CARTE_ETUDIANT,
        AUTRE
    }

    public enum Statut {
        EN_ATTENTE,
        EN_COURS,
        PRET,
        REJETE,
        RECUPERE
    }
}
