package tn.enicarthage.campushub.student.repository;

import tn.enicarthage.campushub.student.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.enicarthage.campushub.student.model.ApplicationStatus;
import tn.enicarthage.campushub.student.model.ClubStatus;
import tn.enicarthage.campushub.student.model.Club;
import tn.enicarthage.campushub.student.model.ClubApplication;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;

    // Get current logged in student
    private Student getCurrentStudent() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // ─── REGULAR STUDENT ───────────────────────────────────────

    // Browse all active clubs
    public List<Club> getActiveClubs() {
        return clubRepository.findByStatus(ClubStatus.ACTIVE);
    }

    // Get single club
    public Club getClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club not found"));
    }

    // Apply to a club
    public String applyToClub(Long clubId, String motivation) {
        Student student = getCurrentStudent();

        Club club = getClub(clubId);
        if (club.getStatus() != ClubStatus.ACTIVE) {
            throw new RuntimeException("Club is not accepting applications");
        }

        if (applicationRepository.existsByStudentIdAndClubId(
                student.getId(), clubId)) {
            throw new RuntimeException("You have already applied to this club");
        }

        applicationRepository.save(ClubApplication.builder()
                .studentId(student.getId())
                .clubId(clubId)
                .motivation(motivation)
                .status(ApplicationStatus.PENDING)
                .build());

        return "Application submitted to " + club.getName();
    }

    // View my applications and their status
    public List<ClubApplication> getMyApplications() {
        Student student = getCurrentStudent();
        return applicationRepository.findByStudentId(student.getId());
    }

    // ─── CLUB HEAD ─────────────────────────────────────────────

    // Get applications for clubs I manage
    public List<ClubApplication> getApplicationsForMyClub(Long clubId) {
        Student student = getCurrentStudent();
        Club club = getClub(clubId);

        if (!club.getHeadId().equals(student.getId())) {
            throw new RuntimeException("You are not the head of this club");
        }

        return applicationRepository.findByClubId(clubId);
    }

    // Accept or reject an application
    public ClubApplication reviewApplication(Long applicationId, String decision) {
        Student student = getCurrentStudent();

        ClubApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Club club = getClub(application.getClubId());
        if (!club.getHeadId().equals(student.getId())) {
            throw new RuntimeException("You are not the head of this club");
        }

        application.setStatus(
                decision.equalsIgnoreCase("accept")
                        ? ApplicationStatus.ACCEPTED
                        : ApplicationStatus.REJECTED
        );

        return applicationRepository.save(application);
    }

    // Update club profile (club head only)
    public Club updateClub(Long clubId, Club updated) {
        Student student = getCurrentStudent();
        Club club = getClub(clubId);

        if (!club.getHeadId().equals(student.getId())) {
            throw new RuntimeException("You are not the head of this club");
        }

        club.setName(updated.getName());
        club.setDescription(updated.getDescription());
        club.setCategory(updated.getCategory());

        return clubRepository.save(club);
    }
}