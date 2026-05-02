package tn.enicarthage.campushub.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String role;     // ETUDIANT, ENSEIGNANT, ADMIN
    private String departement;
}
