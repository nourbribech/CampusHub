package tn.enicarthage.campushub.admin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;          // added — replaces String demandeur

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(length = 1000)
    private String detail;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.EN_ATTENTE;

    @Column(name = "commentaire_admin")
    private String commentaireAdmin; // maps to adminNote in student version

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (submittedAt == null) submittedAt = LocalDateTime.now();
    }

    public enum Type {
        CERTIFICAT,
        REMBOURSEMENT,
        MATERIEL,
        RESERVATION_SALLE,  // covers student's ROOM_BOOKING
        OTHER
    }

    public enum Statut {
        EN_ATTENTE,
        APPROUVE,
        REJETE
    }
}