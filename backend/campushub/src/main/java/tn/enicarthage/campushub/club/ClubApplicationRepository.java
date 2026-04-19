package tn.enicarthage.campushub.club;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Long> {
    List<ClubApplication> findByStudentId(Long studentId);
    List<ClubApplication> findByClubId(Long clubId);
    Optional<ClubApplication> findByStudentIdAndClubId(Long studentId, Long clubId);
    boolean existsByStudentIdAndClubId(Long studentId, Long clubId);
}