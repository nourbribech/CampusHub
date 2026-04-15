package tn.enicarthage.campushub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.dto.CreateReservationDto;
import tn.enicarthage.campushub.dto.ReservationDto;
import tn.enicarthage.campushub.model.Reservation;
import tn.enicarthage.campushub.model.Salle;
import tn.enicarthage.campushub.model.User;
import tn.enicarthage.campushub.service.ReservationService;
import tn.enicarthage.campushub.service.SalleService;
import tn.enicarthage.campushub.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;
    private final SalleService salleService;

    @GetMapping("/me")
    public ResponseEntity<List<ReservationDto>> getMesReservations() {
        log.info("GET /api/v1/reservations/me");

        // Pour l'instant, on récupère les réservations du premier enseignant
        // TODO: Récupérer l'utilisateur connecté depuis le contexte de sécurité
        User enseignant = userService.getUsersByRole(User.Role.ENSEIGNANT)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun enseignant trouvé"));

        List<Reservation> reservations = reservationService.getReservationsByEnseignant(enseignant.getId());
        List<ReservationDto> dtos = reservations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        log.info("GET /api/v1/reservations/{}", id);

        Reservation reservation = reservationService.getReservationById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        return ResponseEntity.ok(convertToDto(reservation));
    }

    @PostMapping
    public ResponseEntity<ReservationDto> creerReservation(@RequestBody CreateReservationDto dto) {
        log.info("POST /api/v1/reservations");

        // Récupérer l'enseignant (pour l'instant le premier)
        User enseignant = userService.getUsersByRole(User.Role.ENSEIGNANT)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

        // Récupérer la salle
        Salle salle = salleService.getSalleById(Long.parseLong(dto.getSalleId()))
                .orElseThrow(() -> new RuntimeException("Salle non trouvée"));

        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setEnseignant(enseignant);
        reservation.setSalle(salle);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setMotif(dto.getMotif());
        reservation.setNombreParticipants(dto.getNombreParticipants());
        reservation.setStatut(Reservation.Statut.EN_ATTENTE);

        Reservation savedReservation = reservationService.createReservation(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedReservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> annulerReservation(@PathVariable Long id) {
        log.info("DELETE /api/v1/reservations/{}", id);

        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> modifierReservation(
            @PathVariable Long id,
            @RequestBody CreateReservationDto dto) {
        log.info("PUT /api/v1/reservations/{}", id);

        Reservation reservation = reservationService.getReservationById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setMotif(dto.getMotif());
        reservation.setNombreParticipants(dto.getNombreParticipants());

        Reservation updatedReservation = reservationService.updateReservation(id, reservation);

        return ResponseEntity.ok(convertToDto(updatedReservation));
    }

    private ReservationDto convertToDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId().toString());
        dto.setSalleId(reservation.getSalle().getId().toString());

        // Salle DTO
        tn.enicarthage.campushub.dto.SalleDto salleDto = new tn.enicarthage.campushub.dto.SalleDto();
        salleDto.setId(reservation.getSalle().getId().toString());
        salleDto.setNom(reservation.getSalle().getNom());
        salleDto.setCapacite(reservation.getSalle().getCapacite());
        salleDto.setEquipements(reservation.getSalle().getEquipements());
        salleDto.setBatiment(reservation.getSalle().getBatiment());
        salleDto.setEtage(reservation.getSalle().getEtage());
        salleDto.setDisponible(reservation.getSalle().getDisponible());
        dto.setSalle(salleDto);

        dto.setEnseignantId(reservation.getEnseignant().getId().toString());
        dto.setDateDebut(reservation.getDateDebut());
        dto.setDateFin(reservation.getDateFin());
        dto.setMotif(reservation.getMotif());
        dto.setNombreParticipants(reservation.getNombreParticipants());
        dto.setStatut(reservation.getStatut().name());
        dto.setCommentaireAdmin(reservation.getCommentaireAdmin());
        dto.setCreatedAt(reservation.getCreatedAt());

        return dto;
    }
}