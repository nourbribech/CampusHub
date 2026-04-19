package tn.enicarthage.campushub.club;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    // ─── REGULAR STUDENT ───────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<Club>> getAllClubs() {
        return ResponseEntity.ok(clubService.getActiveClubs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> getClub(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClub(id));
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<String> apply(
            @PathVariable Long id,
            @RequestParam String motivation) {
        return ResponseEntity.ok(clubService.applyToClub(id, motivation));
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<ClubApplication>> myApplications() {
        return ResponseEntity.ok(clubService.getMyApplications());
    }

    // ─── CLUB HEAD ─────────────────────────────────────────────

    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ClubApplication>> clubApplications(
            @PathVariable Long id) {
        return ResponseEntity.ok(clubService.getApplicationsForMyClub(id));
    }

    @PatchMapping("/applications/{applicationId}")
    public ResponseEntity<ClubApplication> review(
            @PathVariable Long applicationId,
            @RequestParam String decision) {
        return ResponseEntity.ok(clubService.reviewApplication(applicationId, decision));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Club> updateClub(
            @PathVariable Long id,
            @RequestBody Club club) {
        return ResponseEntity.ok(clubService.updateClub(id, club));
    }
}