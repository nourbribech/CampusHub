package tn.enicarthage.campushub.student.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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