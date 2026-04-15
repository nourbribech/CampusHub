package tn.enicarthage.campushub.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.admin.dto.DashboardStatsDto;
import tn.enicarthage.campushub.admin.service.AdminService;
import tn.enicarthage.campushub.model.enseignant.Reservation;
import tn.enicarthage.campushub.shared.model.User;

import java.util.List;

/**
 * Controller dédié au domaine Admin — Binôme 3
 * Endpoints :
 *   GET  /api/v1/admin/dashboard
 *   GET  /api/v1/admin/reservations/pending
 *   PUT  /api/v1/admin/reservations/{id}/approve
 *   PUT  /api/v1/admin/reservations/{id}/reject
 *   GET  /api/v1/admin/users
 *   DELETE /api/v1/admin/users/{id}
 */
@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDto> getDashboard() {
        log.info("GET /api/v1/admin/dashboard");
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/reservations/pending")
    public ResponseEntity<List<Reservation>> getReservationsEnAttente() {
        log.info("GET /api/v1/admin/reservations/pending");
        return ResponseEntity.ok(adminService.getReservationsEnAttente());
    }

    @PutMapping("/reservations/{id}/approve")
    public ResponseEntity<Reservation> approuverReservation(
            @PathVariable Long id,
            @RequestParam(required = false) String commentaire) {
        log.info("PUT /api/v1/admin/reservations/{}/approve", id);
        return ResponseEntity.ok(adminService.approuverReservation(id, commentaire));
    }

    @PutMapping("/reservations/{id}/reject")
    public ResponseEntity<Reservation> rejeterReservation(
            @PathVariable Long id,
            @RequestParam(required = false) String raison) {
        log.info("PUT /api/v1/admin/reservations/{}/reject", id);
        return ResponseEntity.ok(adminService.rejeterReservation(id, raison));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("GET /api/v1/admin/users");
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/v1/admin/users/{}", id);
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
