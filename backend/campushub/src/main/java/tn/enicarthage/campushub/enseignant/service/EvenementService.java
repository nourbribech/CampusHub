package tn.enicarthage.campushub.enseignant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.campushub.enseignant.model.Evenement;
import tn.enicarthage.campushub.enseignant.repository.EvenementRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvenementService {

    private final EvenementRepository evenementRepository;

    public List<Evenement> getAllEvenements() {
        return evenementRepository.findAll();
    }

    public List<Evenement> getEvenementsAVenir() {
        return evenementRepository.findByDateDebutAfter(LocalDateTime.now());
    }

    public Optional<Evenement> getEvenementById(Long id) {
        return evenementRepository.findById(id);
    }

    public List<Evenement> getEvenementsByOrganisateur(Long organisateurId) {
        return evenementRepository.findByOrganisateurId(organisateurId);
    }

    public Evenement createEvenement(Evenement evenement) {
        return evenementRepository.save(evenement);
    }

    public Evenement updateEvenement(Long id, Evenement details) {
        Evenement evenement = evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        evenement.setTitre(details.getTitre());
        evenement.setDescription(details.getDescription());
        evenement.setDateDebut(details.getDateDebut());
        evenement.setDateFin(details.getDateFin());
        evenement.setLieu(details.getLieu());
        evenement.setNbParticipantsMax(details.getNbParticipantsMax());
        evenement.setStatut(details.getStatut());
        evenement.setImageUrl(details.getImageUrl());
        return evenementRepository.save(evenement);
    }

    public void deleteEvenement(Long id) {
        evenementRepository.deleteById(id);
    }

    public long countEvenements() {
        return evenementRepository.count();
    }
}
