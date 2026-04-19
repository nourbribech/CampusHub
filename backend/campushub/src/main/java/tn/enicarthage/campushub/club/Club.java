package tn.enicarthage.campushub.club;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.enicarthage.campushub.model.enums.ClubStatus;

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
    private String name;

    @Column(length = 1000)
    private String description;

    private String category; // e.g. "Tech", "Sports", "Arts"

    private Long headId; // student ID of the club head

    @Enumerated(EnumType.STRING)
    private ClubStatus status; // ACTIVE, INACTIVE
}