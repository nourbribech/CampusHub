package tn.enicarthage.campushub.shared.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clubs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;           // used by admin

    @Column(length = 1000)
    private String description;

    private String categorie;     // used by admin ("Tech", "Sports"...)

    private String responsable;   // used by admin

    private Integer nombreMembres = 0;

    private Long headId;          // used by student (club head's user ID)

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.EN_ATTENTE;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum Statut {
        EN_ATTENTE, ACTIF, SUSPENDU
    }
}