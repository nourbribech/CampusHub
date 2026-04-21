package tn.enicarthage.campushub.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.campushub.student.model.ClubStatus;
import tn.enicarthage.campushub.student.model.Club;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByStatus(ClubStatus status);
    List<Club> findByHeadId(Long headId);
}