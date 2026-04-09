package tn.enicarthage.campushub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalleDto {
    private String id;
    private String nom;
    private Integer capacite;
    private List<String> equipements;
    private String batiment;
    private Integer etage;
    private Boolean disponible;
    private String imageUrl;
}