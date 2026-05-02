package tn.enicarthage.campushub.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.enseignant.model.Evenement;
import tn.enicarthage.campushub.student.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController("studentEvenementController")
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class StudentEvenementController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<Evenement>> getAllEvenements() {
        return ResponseEntity.ok(eventService.getApprovedEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evenement> getEvenement(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<String> register(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.registerForEvent(id));
    }

    @DeleteMapping("/{id}/register")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.cancelRegistration(id));
    }

    @PostMapping("/submit")
    public ResponseEntity<Evenement> submit(
            @RequestParam String titre,
            @RequestParam String description,
            @RequestParam String lieu,
            @RequestParam LocalDateTime dateDebut,
            @RequestParam LocalDateTime dateFin,
            @RequestParam int nbParticipantsMax) {
        return ResponseEntity.ok(eventService.submitEvent(
                titre, description, lieu, dateDebut, dateFin, nbParticipantsMax));
    }

    @GetMapping("/my-registrations")
    public ResponseEntity<List<Evenement>> myRegistrations() {
        return ResponseEntity.ok(eventService.getMyRegistrations());
    }
    @GetMapping("/approved")
    public ResponseEntity<List<Evenement>> getApproved() {
        return ResponseEntity.ok(eventService.getApprovedEvents());
    }
}