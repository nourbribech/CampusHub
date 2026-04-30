package tn.enicarthage.campushub.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.campushub.admin.model.Club;
import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByStatut(Club.Statut statut);
    long countByStatut(Club.Statut statut);
}