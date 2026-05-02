package tn.enicarthage.campushub.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
    private String departement;
    private String avatar;
}
