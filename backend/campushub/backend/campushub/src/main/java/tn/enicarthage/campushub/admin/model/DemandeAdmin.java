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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String detail;

    @Column(nullable = false)
    private String demandeur;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.EN_ATTENTE;

    @Column(name = "commentaire_admin")
    private String commentaireAdmin;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum Type { CERTIFICAT, REMBOURSEMENT, MATERIEL }
    public enum Statut { EN_ATTENTE, APPROUVE, REJETE }
}
