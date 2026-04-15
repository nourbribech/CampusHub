package tn.enicarthage.campushub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.dto.DemandeDocumentDto;
import tn.enicarthage.campushub.model.DemandeDocument;
import tn.enicarthage.campushub.model.User;
import tn.enicarthage.campushub.service.DemandeDocumentService;
import tn.enicarthage.campushub.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/demandes-documents")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class DemandeDocumentController {

    private final DemandeDocumentService demandeDocumentService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<DemandeDocumentDto>> getAllDemandes() {
        log.info("GET /api/v1/demandes-documents");
        List<DemandeDocument> demandes = demandeDocumentService.getAllDemandes();
        return ResponseEntity.ok(demandes.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/me")
    public ResponseEntity<List<DemandeDocumentDto>> getMesDemandes() {
        log.info("GET /api/v1/demandes-documents/me");
        // Pour l'instant on récupère le premier étudiant
        User etudiant = userService.getUsersByRole(User.Role.ETUDIANT).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        List<DemandeDocument> demandes = demandeDocumentService.getDemandesByEtudiant(etudiant.getId());
        return ResponseEntity.ok(demandes.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<DemandeDocumentDto> creerDemande(@RequestBody DemandeDocumentDto dto) {
        log.info("POST /api/v1/demandes-documents");
        
        User etudiant = userService.getUserById(Long.parseLong(dto.getDemandeurId()))
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        DemandeDocument demande = new DemandeDocument();
        demande.setTypeDocument(DemandeDocument.TypeDocument.valueOf(dto.getTypeDocument()));
        demande.setDemandeur(etudiant);
        
        DemandeDocument saved = demandeDocumentService.createDemande(demande);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(saved));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<DemandeDocumentDto> updateStatut(
            @PathVariable Long id,
            @RequestParam DemandeDocument.Statut statut,
            @RequestParam(required = false) String commentaire) {
        log.info("PATCH /api/v1/demandes-documents/{}/statut", id);
        
        DemandeDocument updated = demandeDocumentService.updateStatut(id, statut, commentaire);
        return ResponseEntity.ok(convertToDto(updated));
    }

    private DemandeDocumentDto convertToDto(DemandeDocument d) {
        DemandeDocumentDto dto = new DemandeDocumentDto();
        dto.setId(d.getId().toString());
        dto.setTypeDocument(d.getTypeDocument().name());
        dto.setDemandeurId(d.getDemandeur().getId().toString());
        dto.setDemandeurNom(d.getDemandeur().getPrenom() + " " + d.getDemandeur().getNom());
        dto.setDateDemande(d.getDateDemande());
        dto.setStatut(d.getStatut().name());
        dto.setCommentaireAdmin(d.getCommentaireAdmin());
        return dto;
    }
}
