package tn.enicarthage.campushub.controller.enseignant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.dto.enseignant.SalleDto;
import tn.enicarthage.campushub.model.enseignant.Salle;
import tn.enicarthage.campushub.service.enseignant.SalleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/salles")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class SalleController {

    private final SalleService salleService;

    @GetMapping
    public ResponseEntity<List<SalleDto>> getAllSalles() {
        log.info("GET /api/v1/salles");
        return ResponseEntity.ok(salleService.getAllSalles().stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalleDto> getSalleById(@PathVariable Long id) {
        log.info("GET /api/v1/salles/{}", id);
        Salle salle = salleService.getSalleById(id)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée"));
        return ResponseEntity.ok(convertToDto(salle));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<SalleDto>> getSallesDisponibles(
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin) {
        log.info("GET /api/v1/salles/disponibles");
        return ResponseEntity.ok(salleService.getSallesDisponibles().stream()
                .map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/batiments")
    public ResponseEntity<List<String>> getBatiments() {
        log.info("GET /api/v1/salles/batiments");
        return ResponseEntity.ok(List.of("Bâtiment Annexe", "Bâtiment Principal"));
    }

    private SalleDto convertToDto(Salle salle) {
        SalleDto dto = new SalleDto();
        dto.setId(salle.getId().toString());
        dto.setNom(salle.getNom());
        dto.setCapacite(salle.getCapacite());
        dto.setEquipements(salle.getEquipements());
        dto.setBatiment(salle.getBatiment());
        dto.setEtage(salle.getEtage());
        dto.setDisponible(salle.getDisponible());
        dto.setImageUrl(salle.getImageUrl());
        return dto;
    }
}