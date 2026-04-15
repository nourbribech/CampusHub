package tn.enicarthage.campushub.model.enseignant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private Integer capacite;

    @ElementCollection
    @CollectionTable(name = "salle_equipements", joinColumns = @JoinColumn(name = "salle_id"))
    @Column(name = "equipement")
    private List<String> equipements = new ArrayList<>();

    @Column(nullable = false)
    private String batiment;

    @Column(nullable = false)
    private Integer etage;

    @Column(nullable = false)
    private Boolean disponible = true;

    private String imageUrl;

    // Relations
    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
}