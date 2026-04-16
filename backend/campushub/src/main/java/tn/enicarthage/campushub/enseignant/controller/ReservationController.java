package tn.enicarthage.campushub.enseignant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.enicarthage.campushub.enseignant.dto.CreateReservationDto;
import tn.enicarthage.campushub.enseignant.dto.ReservationDto;
import tn.enicarthage.campushub.enseignant.dto.SalleDto;
import tn.enicarthage.campushub.enseignant.model.Reservation;
import tn.enicarthage.campushub.enseignant.model.Salle;
import tn.enicarthage.campushub.enseignant.service.ReservationService;
import tn.enicarthage.campushub.enseignant.service.SalleService;
import tn.enicarthage.campushub.enseignant.service.UserService;
import tn.enicarthage.campushub.shared.model.User;

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
        User enseignant = userService.getUsersByRole(User.Role.ENSEIGNANT)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun enseignant trouvé"));
        List<Reservation> reservations = reservationService.getReservationsByEnseignant(enseignant.getId());
        return ResponseEntity.ok(reservations.stream().map(this::convertToDto).collect(Collectors.toList()));
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
        User enseignant = userService.getUsersByRole(User.Role.ENSEIGNANT)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));
        Salle salle = salleService.getSalleById(Long.parseLong(dto.getSalleId()))
                .orElseThrow(() -> new RuntimeException("Salle non trouvée"));
        Reservation reservation = new Reservation();
        reservation.setEnseignant(enseignant);
        reservation.setSalle(salle);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setMotif(dto.getMotif());
        reservation.setNombreParticipants(dto.getNombreParticipants());
        reservation.setStatut(Reservation.Statut.EN_ATTENTE);
        Reservation saved = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> annulerReservation(@PathVariable Long id) {
        log.info("DELETE /api/v1/reservations/{}", id);
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> modifierReservation(
            @PathVariable Long id, @RequestBody CreateReservationDto dto) {
        log.info("PUT /api/v1/reservations/{}", id);
        Reservation reservation = reservationService.getReservationById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setMotif(dto.getMotif());
        reservation.setNombreParticipants(dto.getNombreParticipants());
        return ResponseEntity.ok(convertToDto(reservationService.updateReservation(id, reservation)));
    }

    private ReservationDto convertToDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId().toString());
        dto.setSalleId(reservation.getSalle().getId().toString());
        SalleDto salleDto = new SalleDto();
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