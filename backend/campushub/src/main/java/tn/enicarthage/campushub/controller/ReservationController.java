package tn.enicarthage.campushub.controller;

import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.dto.CreateReservationDto;
import tn.enicarthage.campushub.dto.ReservationDto;
import tn.enicarthage.campushub.dto.SalleDto;
import tn.enicarthage.campushub.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

        private List<ReservationDto> reservations = new ArrayList<>(Arrays.asList(
                        new ReservationDto(
                                        "1", "1",
                                        new SalleDto("1", "Amphithéâtre A", 200, Arrays.asList("Projecteur"),
                                                        "Bâtiment A", 1, true, null),
                                        "user1",
                                        new UserDto("user1", "Dupont", "Jean", "jean.dupont@enicarthage.tn",
                                                        "ENSEIGNANT", "Informatique", null),
                                        LocalDateTime.now().plusDays(3),
                                        LocalDateTime.now().plusDays(3).plusHours(2),
                                        "Cours de Programmation Avancée",
                                        30, "APPROUVEE", null, LocalDateTime.now().minusDays(5)),
                        new ReservationDto(
                                        "2", "2",
                                        new SalleDto("2", "Salle B12", 30, Arrays.asList("Tableau"), "Bâtiment B", 1,
                                                        true, null),
                                        "user1",
                                        new UserDto("user1", "Dupont", "Jean", "jean.dupont@enicarthage.tn",
                                                        "ENSEIGNANT", "Informatique", null),
                                        LocalDateTime.now().plusDays(7),
                                        LocalDateTime.now().plusDays(7).plusHours(3),
                                        "TP Base de Données",
                                        25, "EN_ATTENTE", null, LocalDateTime.now().minusDays(2))));

        @GetMapping("/me")
        public List<ReservationDto> getMesReservations() {
                return reservations;
        }

        @GetMapping("/{id}")
        public ReservationDto getReservationById(@PathVariable String id) {
                return reservations.stream()
                                .filter(r -> r.getId().equals(id))
                                .findFirst()
                                .orElse(null);
        }

        @PostMapping
        public ReservationDto creerReservation(@RequestBody CreateReservationDto dto) {
                System.out.println("POST /api/v1/reservations - 200 OK");
                ReservationDto nouvelle = new ReservationDto();
                nouvelle.setId(UUID.randomUUID().toString());
                nouvelle.setSalleId(dto.getSalleId());
                nouvelle.setSalle(new SalleDto(dto.getSalleId(), "Salle " + dto.getSalleId(),
                                30, Arrays.asList("Equipements"), "Bâtiment", 1, true, null));
                nouvelle.setEnseignantId("user1");
                nouvelle.setEnseignant(new UserDto("user1", "Dupont", "Jean",
                                "jean.dupont@enicarthage.tn", "ENSEIGNANT", "Informatique", null));
                nouvelle.setDateDebut(dto.getDateDebut());
                nouvelle.setDateFin(dto.getDateFin());
                nouvelle.setMotif(dto.getMotif());
                nouvelle.setNombreParticipants(dto.getNombreParticipants());
                nouvelle.setStatut("EN_ATTENTE");
                nouvelle.setCreatedAt(LocalDateTime.now());

                reservations.add(nouvelle);
                return nouvelle;
        }

        @DeleteMapping("/{id}")
        public void annulerReservation(@PathVariable String id) {
                reservations.removeIf(r -> r.getId().equals(id));
        }
}