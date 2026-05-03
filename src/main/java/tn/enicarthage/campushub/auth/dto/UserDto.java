package tn.enicarthage.campushub.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private List<String> roles;
    private String departement;
    private String avatar;
}