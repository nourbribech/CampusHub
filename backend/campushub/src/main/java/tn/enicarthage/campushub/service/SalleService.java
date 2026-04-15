package tn.enicarthage.campushub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.enicarthage.campushub.model.Salle;
import tn.enicarthage.campushub.repository.SalleRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalleService {

    private final SalleRepository salleRepository;

    // ==================== CRUD BASIQUE ====================

    public Salle createSalle(Salle salle) {
        log.info("📝 Création d'une nouvelle salle : {}", salle.getNom());
        Salle savedSalle = salleRepository.save(salle);
        log.info("✅ Salle créée avec succès - ID: {}", savedSalle.getId());
        return savedSalle;
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

        Salle updatedSalle = salleRepository.save(salle);
        log.info("✅ Salle mise à jour avec succès");
        return updatedSalle;
    }

    public void deleteSalle(Long id) {
        log.info("🗑️ Suppression de la salle ID: {}", id);

        if (!salleRepository.existsById(id)) {
            throw new RuntimeException("Salle non trouvée avec l'ID: " + id);
        }

        salleRepository.deleteById(id);
        log.info("✅ Salle supprimée avec succès");
    }

    // ==================== MÉTHODES AVANCÉES ====================

    /**
     * Récupérer les salles par bâtiment
     */
    public List<Salle> getSallesByBatiment(String batiment) {
        log.info("🏢 Recherche des salles du bâtiment : {}", batiment);
        return salleRepository.findByBatiment(batiment);
    }

    /**
     * Récupérer les salles disponibles
     */
    public List<Salle> getSallesDisponibles() {
        log.info("✅ Récupération des salles disponibles");
        return salleRepository.findByDisponibleTrue();
    }

    /**
     * Récupérer les salles par capacité minimale
     */
    public List<Salle> getSallesByCapaciteMin(Integer capaciteMin) {
        log.info("👥 Recherche des salles avec capacité >= {}", capaciteMin);
        return salleRepository.findByCapaciteGreaterThanEqual(capaciteMin);
    }

    /**
     * Recherche avancée : Salles par étage et bâtiment
     */
    public List<Salle> getSallesByBatimentAndEtage(String batiment, Integer etage) {
        log.info("🏢 Recherche des salles : Bâtiment={}, Étage={}", batiment, etage);
        return salleRepository.findAll().stream()
                .filter(s -> batiment.equals(s.getBatiment()))
                .filter(s -> etage.equals(s.getEtage()))
                .toList();
    }

    /**
     * Recherche avancée : Salles avec équipement spécifique
     */
    public List<Salle> getSallesWithEquipement(String equipement) {
        log.info("🔧 Recherche des salles avec l'équipement : {}", equipement);
        return salleRepository.findAll().stream()
                .filter(s -> s.getEquipements().contains(equipement))
                .toList();
    }

    /**
     * Recherche multi-critères
     */
    public List<Salle> searchSalles(String batiment, Integer capaciteMin,
                                    Boolean disponible, String equipement) {
        log.info("🔎 Recherche multi-critères de salles");

        return salleRepository.findAll().stream()
                .filter(s -> batiment == null || batiment.equals(s.getBatiment()))
                .filter(s -> capaciteMin == null || s.getCapacite() >= capaciteMin)
                .filter(s -> disponible == null || disponible.equals(s.getDisponible()))
                .filter(s -> equipement == null || s.getEquipements().contains(equipement))
                .toList();
    }

    /**
     * Statistiques : Répartition des salles par bâtiment
     */
    public Map<String, Long> getStatistiquesParBatiment() {
        log.info("📊 Génération des statistiques par bâtiment");
        return salleRepository.findAll().stream()
                .collect(Collectors.groupingBy(Salle::getBatiment, Collectors.counting()));
    }

    /**
     * Statistiques : Répartition des salles par étage
     */
    public Map<Integer, Long> getStatistiquesParEtage() {
        log.info("📊 Génération des statistiques par étage");
        return salleRepository.findAll().stream()
                .collect(Collectors.groupingBy(Salle::getEtage, Collectors.counting()));
    }

    /**
     * Statistiques : Capacité totale par bâtiment
     */
    public Map<String, Integer> getCapaciteTotaleParBatiment() {
        log.info("📊 Calcul de la capacité totale par bâtiment");
        return salleRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Salle::getBatiment,
                        Collectors.summingInt(Salle::getCapacite)
                ));
    }

    /**
     * Obtenir la salle la plus grande
     */
    public Optional<Salle> getSalleLaPlusGrande() {
        log.info("🏆 Recherche de la salle la plus grande");
        return salleRepository.findAll().stream()
                .max(Comparator.comparing(Salle::getCapacite));
    }

    /**
     * Obtenir les salles triées par capacité
     */
    public List<Salle> getSallesOrderByCapacite(boolean ascending) {
        log.info("📊 Tri des salles par capacité (croissant: {})", ascending);
        List<Salle> salles = salleRepository.findAll();
        salles.sort(ascending ?
                Comparator.comparing(Salle::getCapacite) :
                Comparator.comparing(Salle::getCapacite).reversed()
        );
        return salles;
    }

    /**
     * Obtenir les équipements disponibles dans toutes les salles
     */
    public Set<String> getAllEquipements() {
        log.info("🔧 Récupération de tous les équipements disponibles");
        return salleRepository.findAll().stream()
                .flatMap(s -> s.getEquipements().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Mettre à jour la disponibilité d'une salle
     */
    public void updateDisponibilite(Long id, Boolean disponible) {
        log.info("🔄 Mise à jour de la disponibilité de la salle ID: {} -> {}", id, disponible);

        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée"));

        salle.setDisponible(disponible);
        salleRepository.save(salle);
        log.info("✅ Disponibilité mise à jour avec succès");
    }

    /**
     * Batch update - Marquer plusieurs salles comme indisponibles
     */
    @Transactional
    public void markSallesAsUnavailable(List<Long> salleIds) {
        log.info("🔒 Marquage de {} salles comme indisponibles", salleIds.size());

        for (Long salleId : salleIds) {
            Salle salle = salleRepository.findById(salleId)
                    .orElseThrow(() -> new RuntimeException("Salle non trouvée : " + salleId));
            salle.setDisponible(false);
            salleRepository.save(salle);
        }

        log.info("✅ {} salles marquées comme indisponibles", salleIds.size());
    }

    /**
     * Compter les salles
     */
    public long countSalles() {
        long count = salleRepository.count();
        log.info("📈 Nombre total de salles : {}", count);
        return count;
    }

    /**
     * Compter les salles par bâtiment
     */
    public long countSallesByBatiment(String batiment) {
        long count = salleRepository.findByBatiment(batiment).size();
        log.info("📈 Nombre de salles dans {} : {}", batiment, count);
        return count;
    }

    /**
     * Compter les salles disponibles
     */
    public long countSallesDisponibles() {
        long count = salleRepository.findByDisponibleTrue().size();
        log.info("📈 Nombre de salles disponibles : {}", count);
        return count;
    }

    /**
     * Calculer le taux d'occupation (basé sur la disponibilité)
     */
    public double getTauxOccupation() {
        long total = salleRepository.count();
        long indisponibles = total - salleRepository.findByDisponibleTrue().size();
        double taux = total > 0 ? (double) indisponibles / total * 100 : 0;
        log.info("📊 Taux d'occupation : {:.2f}%", taux);
        return taux;
    }
}