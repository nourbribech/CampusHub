package tn.enicarthage.campushub.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileDto {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String departement;
    private String avatar;
}
