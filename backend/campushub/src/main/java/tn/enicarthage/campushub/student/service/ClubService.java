package tn.enicarthage.campushub.student.service;

import tn.enicarthage.campushub.shared.model.Club;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;
import tn.enicarthage.campushub.student.model.ApplicationStatus;
import tn.enicarthage.campushub.student.model.ClubApplication;
import tn.enicarthage.campushub.student.repository.ClubApplicationRepository;
import tn.enicarthage.campushub.admin.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Club> getActiveClubs() {
        return clubRepository.findByStatut(Club.Statut.ACTIF);
    }

    public Club getClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club not found"));
    }

    public String applyToClub(Long clubId, String motivation) {
        User user = getCurrentUser();
        Club club = getClub(clubId);

        if (club.getStatut() != Club.Statut.ACTIF) {
            throw new RuntimeException("Club is not accepting applications");
        }

        if (applicationRepository.existsByStudentIdAndClubId(user.getId(), clubId)) {
            throw new RuntimeException("You have already applied to this club");
        }

        applicationRepository.save(ClubApplication.builder()
                .studentId(user.getId())
                .clubId(clubId)
                .motivation(motivation)
                .status(ApplicationStatus.PENDING)
                .build());

        return "Application submitted to " + club.getNom();
    }

    public List<ClubApplication> getMyApplications() {
        User user = getCurrentUser();
        return applicationRepository.findByStudentId(user.getId());
    }

    public List<ClubApplication> getApplicationsForMyClub(Long clubId) {
        User user = getCurrentUser();
        Club club = getClub(clubId);

        if (!club.getHeadId().equals(user.getId())) {
            throw new RuntimeException("You are not the head of this club");
        }

        return applicationRepository.findByClubId(clubId);
    }

    public ClubApplication reviewApplication(Long applicationId, String decision) {
        User user = getCurrentUser();
        ClubApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Club club = getClub(application.getClubId());
        if (!club.getHeadId().equals(user.getId())) {
            throw new RuntimeException("You are not the head of this club");
        }

        application.setStatus(
                decision.equalsIgnoreCase("accept")
                        ? ApplicationStatus.ACCEPTED
                        : ApplicationStatus.REJECTED
        );

        return applicationRepository.save(application);
    }

    public Club updateClub(Long clubId, Club updated) {
        User user = getCurrentUser();
        Club club = getClub(clubId);

        if (!club.getHeadId().equals(user.getId())) {
            throw new RuntimeException("You are not the head of this club");
        }

        club.setNom(updated.getNom());
        club.setDescription(updated.getDescription());
        club.setCategorie(updated.getCategorie());

        return clubRepository.save(club);
    }
}