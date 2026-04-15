package tn.enicarthage.campushub.controller.enseignant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.enicarthage.campushub.dto.enseignant.EvenementDto;
import tn.enicarthage.campushub.model.enseignant.Evenement;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.service.enseignant.EvenementService;
import tn.enicarthage.campushub.service.enseignant.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/evenements")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class EvenementController {

    private final EvenementService evenementService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<EvenementDto>> getAllEvenements() {
        log.info("GET /api/v1/evenements");
        List<Evenement> evenements = evenementService.getAllEvenements();
        return ResponseEntity.ok(evenements.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/a-venir")
    public ResponseEntity<List<EvenementDto>> getEvenementsAVenir() {
        log.info("GET /api/v1/evenements/a-venir");
        List<Evenement> evenements = evenementService.getEvenementsAVenir();
        return ResponseEntity.ok(evenements.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvenementDto> getEvenementById(@PathVariable Long id) {
        log.info("GET /api/v1/evenements/{}", id);
        return evenementService.getEvenementById(id)
                .map(e -> ResponseEntity.ok(convertToDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EvenementDto> createEvenement(@RequestBody EvenementDto dto) {
        log.info("POST /api/v1/evenements");

        User organisateur = userService.getUserById(Long.parseLong(dto.getOrganisateurId()))
                .orElseThrow(() -> new RuntimeException("Organisateur non trouvé"));

        Evenement evenement = new Evenement();
        evenement.setTitre(dto.getTitre());
        evenement.setDescription(dto.getDescription());
        evenement.setDateDebut(dto.getDateDebut());
        evenement.setDateFin(dto.getDateFin());
        evenement.setLieu(dto.getLieu());
        evenement.setNbParticipantsMax(dto.getNbParticipantsMax());
        evenement.setOrganisateur(organisateur);
        evenement.setImageUrl(dto.getImageUrl());

        Evenement saved = evenementService.createEvenement(evenement);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvenementDto> updateEvenement(@PathVariable Long id, @RequestBody EvenementDto dto) {
        log.info("PUT /api/v1/evenements/{}", id);

        Evenement details = new Evenement();
        details.setTitre(dto.getTitre());
        details.setDescription(dto.getDescription());
        details.setDateDebut(dto.getDateDebut());
        details.setDateFin(dto.getDateFin());
        details.setLieu(dto.getLieu());
        details.setNbParticipantsMax(dto.getNbParticipantsMax());
        details.setStatut(Evenement.Statut.valueOf(dto.getStatut()));
        details.setImageUrl(dto.getImageUrl());

        Evenement updated = evenementService.updateEvenement(id, details);
        return ResponseEntity.ok(convertToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        log.info("DELETE /api/v1/evenements/{}", id);
        evenementService.deleteEvenement(id);
        return ResponseEntity.noContent().build();
    }

    private EvenementDto convertToDto(Evenement e) {
        EvenementDto dto = new EvenementDto();
        dto.setId(e.getId().toString());
        dto.setTitre(e.getTitre());
        dto.setDescription(e.getDescription());
        dto.setDateDebut(e.getDateDebut());
        dto.setDateFin(e.getDateFin());
        dto.setLieu(e.getLieu());
        dto.setNbParticipantsMax(e.getNbParticipantsMax());
        dto.setStatut(e.getStatut().name());
        dto.setOrganisateurId(e.getOrganisateur().getId().toString());
        dto.setImageUrl(e.getImageUrl());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }
}
