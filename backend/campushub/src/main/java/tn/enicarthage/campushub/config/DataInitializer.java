package tn.enicarthage.campushub.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.enicarthage.campushub.model.DemandeDocument;
import tn.enicarthage.campushub.model.Evenement;
import tn.enicarthage.campushub.model.Reservation;
import tn.enicarthage.campushub.model.Salle;
import tn.enicarthage.campushub.model.User;
import tn.enicarthage.campushub.service.DemandeDocumentService;
import tn.enicarthage.campushub.service.EvenementService;
import tn.enicarthage.campushub.service.ReservationService;
import tn.enicarthage.campushub.service.SalleService;
import tn.enicarthage.campushub.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final SalleService salleService;
    private final ReservationService reservationService;
    private final EvenementService evenementService;
    private final DemandeDocumentService demandeDocumentService;

    @Override
    public void run(String... args) {
        log.info("🚀 Initialisation des données...");

        if (userService.countUsers() > 0) {
            log.info("✅ Données déjà présentes, initialisation annulée");
            return;
        }

        try {
            // Créer des utilisateurs
            createUsers();

            // Créer des salles
            createSalles();

            // Créer des réservations
            createReservations();

            // Créer des événements
            createEvents();

            // Créer des demandes de documents
            createDemandes();

            log.info("✅ Initialisation des données terminée avec succès !");

        } catch (Exception e) {
            log.error("❌ Erreur lors de l'initialisation des données", e);
        }
    }

    private void createUsers() {
        log.info("📝 Création des utilisateurs...");

        // Admin
        User admin = new User();
        admin.setNom("Admin");
        admin.setPrenom("System");
        admin.setEmail("admin@enicarthage.tn");
        admin.setPassword("admin123");
        admin.setRole(User.Role.ADMIN);
        admin.setDepartement("Administration");
        userService.createUser(admin);

        // Enseignants
        User enseignant1 = new User();
        enseignant1.setNom("Dupont");
        enseignant1.setPrenom("Jean");
        enseignant1.setEmail("jean.dupont@enicarthage.tn");
        enseignant1.setPassword("enseignant123");
        enseignant1.setRole(User.Role.ENSEIGNANT);
        enseignant1.setDepartement("Informatique");
        userService.createUser(enseignant1);

        User enseignant2 = new User();
        enseignant2.setNom("Martin");
        enseignant2.setPrenom("Sophie");
        enseignant2.setEmail("sophie.martin@enicarthage.tn");
        enseignant2.setPassword("enseignant123");
        enseignant2.setRole(User.Role.ENSEIGNANT);
        enseignant2.setDepartement("Mécatronique");
        userService.createUser(enseignant2);

        // Étudiant
        User etudiant = new User();
        etudiant.setNom("Ben Ali");
        etudiant.setPrenom("Ahmed");
        etudiant.setEmail("ahmed.benali@enicarthage.tn");
        etudiant.setPassword("etudiant123");
        etudiant.setRole(User.Role.ETUDIANT);
        etudiant.setDepartement("Informatique");
        userService.createUser(etudiant);

        log.info("✅ {} utilisateurs créés", userService.countUsers());
    }

    private void createSalles() {
        log.info("📝 Création des salles...");

        // Bâtiment Annexe - RDC
        Salle mac = new Salle();
        mac.setNom("Salle MAC");
        mac.setCapacite(30);
        mac.setEquipements(Arrays.asList("Ordinateurs Mac", "Projecteur", "Tableau interactif"));
        mac.setBatiment("Bâtiment Annexe");
        mac.setEtage(0);
        mac.setDisponible(true);
        salleService.createSalle(mac);

        Salle labo = new Salle();
        labo.setNom("LABO Informatique");
        labo.setCapacite(25);
        labo.setEquipements(Arrays.asList("Ordinateurs", "Serveurs", "Switch réseau"));
        labo.setBatiment("Bâtiment Annexe");
        labo.setEtage(0);
        labo.setDisponible(true);
        salleService.createSalle(labo);

        // Bâtiment Annexe - 1er étage
        for (int i = 0; i <= 7; i++) {
            Salle salle = new Salle();
            salle.setNom("Salle " + (20 + i));
            salle.setCapacite(35);
            salle.setEquipements(Arrays.asList("Projecteur", "Tableau blanc", "WiFi"));
            salle.setBatiment("Bâtiment Annexe");
            salle.setEtage(1);
            salle.setDisponible(true);
            salleService.createSalle(salle);
        }

        // Bâtiment Principal - Amphi
        Salle amphi = new Salle();
        amphi.setNom("Amphithéâtre Principal");
        amphi.setCapacite(250);
        amphi.setEquipements(Arrays.asList("Projecteur", "Micro", "Système audio", "Écran géant"));
        amphi.setBatiment("Bâtiment Principal");
        amphi.setEtage(0);
        amphi.setDisponible(true);
        salleService.createSalle(amphi);

        log.info("✅ {} salles créées", salleService.countSalles());
    }

    private void createReservations() {
        log.info("📝 Création des réservations...");

        User enseignant = userService.getUserByEmail("jean.dupont@enicarthage.tn")
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

        Salle salle = salleService.getSallesByBatiment("Bâtiment Annexe").get(0);

        // Réservation 1 - Approuvée
        Reservation reservation1 = new Reservation();
        reservation1.setEnseignant(enseignant);
        reservation1.setSalle(salle);
        reservation1.setDateDebut(LocalDateTime.now().plusDays(3).withHour(10).withMinute(0));
        reservation1.setDateFin(LocalDateTime.now().plusDays(3).withHour(12).withMinute(0));
        reservation1.setMotif("Cours de Programmation Avancée");
        reservation1.setNombreParticipants(30);
        reservation1.setStatut(Reservation.Statut.APPROUVEE);
        reservationService.createReservation(reservation1);

        // Réservation 2 - En attente
        Reservation reservation2 = new Reservation();
        reservation2.setEnseignant(enseignant);
        reservation2.setSalle(salle);
        reservation2.setDateDebut(LocalDateTime.now().plusDays(7).withHour(14).withMinute(0));
        reservation2.setDateFin(LocalDateTime.now().plusDays(7).withHour(17).withMinute(0));
        reservation2.setMotif("TP Base de Données");
        reservation2.setNombreParticipants(25);
        reservation2.setStatut(Reservation.Statut.EN_ATTENTE);
        reservationService.createReservation(reservation2);

        log.info("✅ {} réservations créées", reservationService.countReservations());
    }

    private void createEvents() {
        log.info("📝 Création des événements...");

        User admin = userService.getUserByEmail("admin@enicarthage.tn")
                .orElseThrow(() -> new RuntimeException("Admin non trouvé"));

        // Événement 1
        Evenement event1 = new Evenement();
        event1.setTitre("Welcome Day ENICarthage");
        event1.setDescription("Journée d'accueil pour les nouveaux étudiants.");
        event1.setDateDebut(LocalDateTime.now().plusDays(10).withHour(9).withMinute(0));
        event1.setDateFin(LocalDateTime.now().plusDays(10).withHour(17).withMinute(0));
        event1.setLieu("Amphithéâtre Principal");
        event1.setNbParticipantsMax(200);
        event1.setOrganisateur(admin);
        event1.setStatut(Evenement.Statut.OUVERT);
        evenementService.createEvenement(event1);

        // Événement 2
        Evenement event2 = new Evenement();
        event2.setTitre("Hackathon CampusHub");
        event2.setDescription("Compétition de développement pour améliorer la vie étudiante.");
        event2.setDateDebut(LocalDateTime.now().plusWeeks(2).withHour(18).withMinute(0));
        event2.setDateFin(LocalDateTime.now().plusWeeks(2).plusDays(2).withHour(12).withMinute(0));
        event2.setLieu("Salle MAC");
        event2.setNbParticipantsMax(50);
        event2.setOrganisateur(admin);
        event2.setStatut(Evenement.Statut.OUVERT);
        evenementService.createEvenement(event2);

        log.info("✅ {} événements créés", evenementService.countEvenements());
    }

    private void createDemandes() {
        log.info("📝 Création des demandes de documents...");

        User etudiant = userService.getUserByEmail("ahmed.benali@enicarthage.tn")
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        // Demande 1
        DemandeDocument demande1 = new DemandeDocument();
        demande1.setTypeDocument(DemandeDocument.TypeDocument.CERTIFICAT_SCOLARITE);
        demande1.setDemandeur(etudiant);
        demande1.setStatut(DemandeDocument.Statut.PRET);
        demande1.setCommentaireAdmin("Votre certificat est prêt à être récupéré au bureau d'ordre.");
        demandeDocumentService.createDemande(demande1);

        // Demande 2
        DemandeDocument demande2 = new DemandeDocument();
        demande2.setTypeDocument(DemandeDocument.TypeDocument.RELEVE_NOTES);
        demande2.setDemandeur(etudiant);
        demande2.setStatut(DemandeDocument.Statut.EN_ATTENTE);
        demandeDocumentService.createDemande(demande2);

        log.info("✅ {} demandes de documents créées", demandeDocumentService.countDemandes());
    }
}