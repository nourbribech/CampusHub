package tn.enicarthage.campushub.club;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.enicarthage.campushub.model.enums.ApplicationStatus;

@Entity
@Table(name = "club_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long clubId;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // PENDING, ACCEPTED, REJECTED

    @Column(length = 500)
    private String motivation; // why they want to join
}