package tn.enicarthage.campushub.student.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.campushub.model.enseignant.DemandeDocument;
import tn.enicarthage.campushub.model.enseignant.Evenement;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.student.service.StudentService;

import java.util.List;

/**
 * Controller dédié au domaine Étudiant — Binôme 2
 * Endpoints :
 * GET /api/v1/student/profile/{id}
 * GET /api/v1/student/evenements
 * POST /api/v1/student/demandes
 * GET /api/v1/student/demandes/{studentId}
 */
@RestController
@RequestMapping("/api/v1/student")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        log.info("GET /api/v1/student/profile/{}", id);
        return studentService.getStudentProfile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/evenements")
    public ResponseEntity<List<Evenement>> getEvenementsDisponibles() {
        log.info("GET /api/v1/student/evenements");
        return ResponseEntity.ok(studentService.getEvenementsDisponibles());
    }

    @PostMapping("/demandes")
    public ResponseEntity<DemandeDocument> soumettreDemandeDocument(
            @RequestParam Long studentId,
            @RequestParam DemandeDocument.TypeDocument type) {
        log.info("POST /api/v1/student/demandes - studentId={}, type={}", studentId, type);
        return ResponseEntity.ok(studentService.soumettreDemandeDocument(studentId, type));
    }

    @GetMapping("/demandes/{studentId}")
    public ResponseEntity<List<DemandeDocument>> getMesDemandes(@PathVariable Long studentId) {
        log.info("GET /api/v1/student/demandes/{}", studentId);
        return ResponseEntity.ok(studentService.getMesDemandes(studentId));
    }
}
