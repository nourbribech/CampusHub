package tn.enicarthage.campushub.service.enseignant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.enicarthage.campushub.model.enseignant.Salle;
import tn.enicarthage.campushub.repository.enseignant.SalleRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalleService {

    private final SalleRepository salleRepository;

    public Salle createSalle(Salle salle) {
        log.info("📝 Création d'une nouvelle salle : {}", salle.getNom());
        return salleRepository.save(salle);
    }

    public List<Salle> getAllSalles() {
        log.info("📋 Récupération de toutes les salles");
        return salleRepository.findAll();
    }

    public Optional<Salle> getSalleById(Long id) {
        log.info("🔍 Recherche de la salle avec ID: {}", id);
        return salleRepository.findById(id);
    }

    public Salle updateSalle(Long id, Salle salleDetails) {
        log.info("✏️ Mise à jour de la salle ID: {}", id);
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée avec l'ID: " + id));
        salle.setNom(salleDetails.getNom());
        salle.setCapacite(salleDetails.getCapacite());
        salle.setEquipements(salleDetails.getEquipements());
        salle.setBatiment(salleDetails.getBatiment());
        salle.setEtage(salleDetails.getEtage());
        salle.setDisponible(salleDetails.getDisponible());
        salle.setImageUrl(salleDetails.getImageUrl());
        return salleRepository.save(salle);
    }

    public void deleteSalle(Long id) {
        log.info("🗑️ Suppression de la salle ID: {}", id);
        if (!salleRepository.existsById(id)) {
            throw new RuntimeException("Salle non trouvée avec l'ID: " + id);
        }
        salleRepository.deleteById(id);
    }

    public List<Salle> getSallesByBatiment(String batiment) {
        return salleRepository.findByBatiment(batiment);
    }

    public List<Salle> getSallesDisponibles() {
        return salleRepository.findByDisponibleTrue();
    }

    public List<Salle> getSallesByCapaciteMin(Integer capaciteMin) {
        return salleRepository.findByCapaciteGreaterThanEqual(capaciteMin);
    }

    public List<Salle> searchSalles(String batiment, Integer capaciteMin, Boolean disponible, String equipement) {
        return salleRepository.findAll().stream()
                .filter(s -> batiment == null || batiment.equals(s.getBatiment()))
                .filter(s -> capaciteMin == null || s.getCapacite() >= capaciteMin)
                .filter(s -> disponible == null || disponible.equals(s.getDisponible()))
                .filter(s -> equipement == null || s.getEquipements().contains(equipement))
                .toList();
    }

    public void updateDisponibilite(Long id, Boolean disponible) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée"));
        salle.setDisponible(disponible);
        salleRepository.save(salle);
    }

    public long countSalles() {
        return salleRepository.count();
    }

    public long countSallesDisponibles() {
        return salleRepository.findByDisponibleTrue().size();
    }

    public Map<String, Long> getStatistiquesParBatiment() {
        return salleRepository.findAll().stream()
                .collect(Collectors.groupingBy(Salle::getBatiment, Collectors.counting()));
    }

    public Set<String> getAllEquipements() {
        return salleRepository.findAll().stream()
                .flatMap(s -> s.getEquipements().stream())
                .collect(Collectors.toSet());
    }
}