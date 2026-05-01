package tn.enicarthage.campushub.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.admin.dto.AdminStatsDto;
import tn.enicarthage.campushub.admin.dto.RejectRequest;
import tn.enicarthage.campushub.admin.model.*;
import tn.enicarthage.campushub.admin.service.AdminService;
import tn.enicarthage.campushub.enseignant.model.Evenement;
import tn.enicarthage.campushub.enseignant.model.Reservation;
import tn.enicarthage.campushub.shared.model.Club;
import tn.enicarthage.campushub.shared.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    // ── STATS ──
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    // ── USERS ──
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // ── EVENTS ──
    @GetMapping("/events")
    public ResponseEntity<List<Evenement>> getAllEvenements() {
        return ResponseEntity.ok(adminService.getAllEvenements());
    }

    @GetMapping("/events/pending")
    public ResponseEntity<List<Evenement>> getPendingEvenements() {
        return ResponseEntity.ok(adminService.getPendingEvenements());
    }

    @PutMapping("/events/{id}/approuver")
    public ResponseEntity<Evenement> approuverEvenement(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approuverEvenement(id));
    }

    @PutMapping("/events/{id}/rejeter")
    public ResponseEntity<Evenement> rejeterEvenement(
            @PathVariable Long id,
            @RequestBody RejectRequest request) {
        return ResponseEntity.ok(adminService.rejeterEvenement(id, request.getCommentaire()));
    }

    // ── RESERVATIONS ──
    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(adminService.getAllReservations());
    }

    @PutMapping("/reservations/{id}/approuver")
    public ResponseEntity<Reservation> approuverReservation(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approuverReservation(id));
    }

    @PutMapping("/reservations/{id}/rejeter")
    public ResponseEntity<Reservation> rejeterReservation(
            @PathVariable Long id,
            @RequestBody RejectRequest request) {
        return ResponseEntity.ok(adminService.rejeterReservation(id, request.getCommentaire()));
    }

    // ── CLUBS ──
    @GetMapping("/clubs")
    public ResponseEntity<List<Club>> getAllClubs() {
        return ResponseEntity.ok(adminService.getAllClubs());
    }

    @PutMapping("/clubs/{id}/approuver")
    public ResponseEntity<Club> approuverClub(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approuverClub(id));
    }

    @PutMapping("/clubs/{id}/suspendre")
    public ResponseEntity<Club> suspendreClub(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.suspendreClub(id));
    }

    // ── DEMANDES ──
    @GetMapping("/demandes")
    public ResponseEntity<List<DemandeAdmin>> getAllDemandes() {
        return ResponseEntity.ok(adminService.getAllDemandes());
    }

    @PutMapping("/demandes/{id}/approuver")
    public ResponseEntity<DemandeAdmin> approuverDemande(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approuverDemande(id));
    }

    @PutMapping("/demandes/{id}/rejeter")
    public ResponseEntity<DemandeAdmin> rejeterDemande(
            @PathVariable Long id,
            @RequestBody RejectRequest request) {
        return ResponseEntity.ok(adminService.rejeterDemande(id, request.getCommentaire()));
    }
}
