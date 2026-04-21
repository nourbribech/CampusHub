package tn.enicarthage.campushub.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.campushub.student.model.ClubApplication;

import java.util.List;
import java.util.Optional;

public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Long> {
    List<ClubApplication> findByStudentId(Long studentId);
    List<ClubApplication> findByClubId(Long clubId);
    Optional<ClubApplication> findByStudentIdAndClubId(Long studentId, Long clubId);
    boolean existsByStudentIdAndClubId(Long studentId, Long clubId);
}