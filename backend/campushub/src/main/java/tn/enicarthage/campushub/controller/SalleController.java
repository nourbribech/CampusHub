package tn.enicarthage.campushub.controller;

import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.dto.SalleDto;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/salles")
@CrossOrigin(origins = "http://localhost:4200")
public class SalleController {

    @GetMapping
    public List<SalleDto> getAllSalles() {
        return Arrays.asList(
                new SalleDto("1", "Amphithéâtre A", 200,
                        Arrays.asList("Projecteur", "Micro", "Climatisation"),
                        "Bâtiment A", 1, true, null),
                new SalleDto("2", "Salle B12", 30,
                        Arrays.asList("Tableau", "Ordinateurs", "WiFi"),
                        "Bâtiment B", 1, true, null),
                new SalleDto("3", "Lab Innovation", 25,
                        Arrays.asList("Projecteur", "Tableau interactif", "WiFi"),
                        "Bâtiment C", 2, true, null),
                new SalleDto("4", "Salle de Conférence", 50,
                        Arrays.asList("Projecteur", "Visioconférence", "Tableau"),
                        "Bâtiment A", 2, true, null)
        );
    }

    @GetMapping("/{id}")
    public SalleDto getSalleById(@PathVariable String id) {
        return getAllSalles().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/disponibles")
    public List<SalleDto> getSallesDisponibles(
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin) {
        return getAllSalles().stream()
                .filter(SalleDto::getDisponible)
                .toList();
    }
}