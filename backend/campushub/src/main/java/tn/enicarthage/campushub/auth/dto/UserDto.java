package tn.enicarthage.campushub.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

// new file: auth/dto/UserDto.java
@Data
@Builder
public class UserDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private List<String> roles;  // ["ETUDIANT", "RESPONSABLE_CLUB"]
    private String avatar;
    private String departement;
}
