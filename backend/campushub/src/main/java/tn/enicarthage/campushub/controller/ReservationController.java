package tn.enicarthage.campushub.controller;

import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.dto.SalleDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/salles")
@CrossOrigin(origins = "http://localhost:4200")
public class SalleController {

        @GetMapping
        public List<SalleDto> getAllSalles() {
                List<SalleDto> salles = new ArrayList<>();

                // ==================== BÂTIMENT ANNEXE (Informatique) ====================

                // RDC Annexe
                salles.add(new SalleDto("annexe-mac", "Salle MAC", 30,
                                Arrays.asList("Ordinateurs Mac", "Projecteur", "Tableau interactif", "Climatisation"),
                                "Bâtiment Annexe", 0, true, null));

                salles.add(new SalleDto("annexe-labo", "LABO Informatique", 25,
                                Arrays.asList("Ordinateurs", "Serveurs", "Switch réseau", "Climatisation"),
                                "Bâtiment Annexe", 0, true, null));

                salles.add(new SalleDto("annexe-polyvalente", "Salle Polyvalente", 150,
                                Arrays.asList("Projecteur", "Sono", "Scène", "Chaises mobiles", "Climatisation"),
                                "Bâtiment Annexe", 0, true, null));

                // 1er Étage Annexe (Salles 20-27)
                for (int i = 0; i <= 7; i++) {
                        int numSalle = 20 + i;
                        salles.add(new SalleDto(
                                        "annexe-" + numSalle,
                                        "Salle " + numSalle,
                                        35,
                                        Arrays.asList("Projecteur", "Tableau blanc", "WiFi", "Climatisation"),
                                        "Bâtiment Annexe",
                                        1,
                                        true,
                                        null));
                }

                // 2ème Étage Annexe (Salles 30-37)
                for (int i = 0; i <= 7; i++) {
                        int numSalle = 30 + i;
                        salles.add(new SalleDto(
                                        "annexe-" + numSalle,
                                        "Salle " + numSalle,
                                        35,
                                        Arrays.asList("Projecteur", "Tableau blanc", "WiFi", "Climatisation"),
                                        "Bâtiment Annexe",
                                        2,
                                        true,
                                        null));
                }

                // ==================== BÂTIMENT PRINCIPAL (Mécatronique & Industriel)
                // ====================

                // RDC Principal
                salles.add(new SalleDto("principal-amphi", "Amphithéâtre Principal", 250,
                                Arrays.asList("Projecteur", "Micro", "Système audio", "Écran géant", "Climatisation"),
                                "Bâtiment Principal", 0, true, null));

                // 1er Étage Principal (Salles 20-27)
                for (int i = 0; i <= 7; i++) {
                        int numSalle = 20 + i;
                        salles.add(new SalleDto(
                                        "principal-" + numSalle,
                                        "Salle " + numSalle,
                                        40,
                                        Arrays.asList("Projecteur", "Tableau blanc", "WiFi", "Climatisation"),
                                        "Bâtiment Principal",
                                        1,
                                        true,
                                        null));
                }

                // 2ème Étage Principal (Salles 30-37)
                for (int i = 0; i <= 7; i++) {
                        int numSalle = 30 + i;
                        salles.add(new SalleDto(
                                        "principal-" + numSalle,
                                        "Salle " + numSalle,
                                        40,
                                        Arrays.asList("Projecteur", "Tableau blanc", "WiFi", "Climatisation"),
                                        "Bâtiment Principal",
                                        2,
                                        true,
                                        null));
                }

                // 3ème Étage Principal (Salles 40-47)
                for (int i = 0; i <= 7; i++) {
                        int numSalle = 40 + i;
                        salles.add(new SalleDto(
                                        "principal-" + numSalle,
                                        "Salle " + numSalle,
                                        40,
                                        Arrays.asList("Projecteur", "Tableau blanc", "WiFi"),
                                        "Bâtiment Principal",
                                        3,
                                        true,
                                        null));
                }

                return salles;
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

        @GetMapping("/batiments")
        public List<String> getBatiments() {
                return Arrays.asList("Bâtiment Annexe", "Bâtiment Principal");
        }
}