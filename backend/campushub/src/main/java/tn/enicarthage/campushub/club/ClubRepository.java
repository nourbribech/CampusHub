package tn.enicarthage.campushub.club;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.campushub.model.enums.ClubStatus;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByStatus(ClubStatus status);
    List<Club> findByHeadId(Long headId);
}