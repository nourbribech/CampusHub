package tn.enicarthage.campushub.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.enicarthage.campushub.enseignant.model.DemandeDocument;
import tn.enicarthage.campushub.enseignant.model.Evenement;
import tn.enicarthage.campushub.enseignant.model.Reservation;
import tn.enicarthage.campushub.enseignant.model.Salle;
import tn.enicarthage.campushub.enseignant.service.DemandeDocumentService;
import tn.enicarthage.campushub.enseignant.service.EvenementService;
import tn.enicarthage.campushub.enseignant.service.ReservationService;
import tn.enicarthage.campushub.enseignant.service.SalleService;
import tn.enicarthage.campushub.shared.service.UserService;
import tn.enicarthage.campushub.shared.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
        log.info("🚀 Initialisation des données ENICarthage...");

       if (userService.countUsers() > 0) {
          log.info("✅ Données déjà présentes, initialisation annulée");

        return;

    }

        try {
            createUsers();
            createSalles();
            createReservations();
            createEvents();
            createDemandes();
            log.info("✅ Initialisation des données terminée avec succès !");
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'initialisation des données", e);
        }
    }

    private void createUsers() {
        log.info("📝 Création des utilisateurs...");

        User admin = new User();
        admin.setNom("Admin");
        admin.setPrenom("System");
        admin.setEmail("admin@enicarthage.tn");
        admin.setPassword("admin123");
        admin.setRoles(List.of(User.Role.ADMIN));
        admin.setDepartement("Administration");
        userService.createUser(admin);

        // Enseignants Informatique (Bâtiment Annexe)
        User enseignant1 = new User();
        enseignant1.setNom("Dupont");
        enseignant1.setPrenom("Jean");
        enseignant1.setEmail("jean.dupont@enicarthage.tn");
        enseignant1.setPassword("enseignant123");
        enseignant1.setRoles(List.of(User.Role.ENSEIGNANT));
        enseignant1.setDepartement("Informatique");
        userService.createUser(enseignant1);

        User enseignant2 = new User();
        enseignant2.setNom("Ben Salah");
        enseignant2.setPrenom("Sarra");
        enseignant2.setEmail("sarra.bensalah@enicarthage.tn");
        enseignant2.setPassword("enseignant123");
        enseignant1.setRoles(List.of(User.Role.ENSEIGNANT));
        enseignant2.setDepartement("Informatique");
        userService.createUser(enseignant2);

        // Enseignants Mécatronique (Bâtiment Principal)
        User enseignant3 = new User();
        enseignant3.setNom("Martin");
        enseignant3.setPrenom("Sophie");
        enseignant3.setEmail("sophie.martin@enicarthage.tn");
        enseignant3.setPassword("enseignant123");
        enseignant1.setRoles(List.of(User.Role.ENSEIGNANT));
        enseignant3.setDepartement("Mécatronique");
        userService.createUser(enseignant3);

        User enseignant4 = new User();
        enseignant4.setNom("Gharbi");
        enseignant4.setPrenom("Karim");
        enseignant4.setEmail("karim.gharbi@enicarthage.tn");
        enseignant4.setPassword("enseignant123");
        enseignant1.setRoles(List.of(User.Role.ENSEIGNANT));
        enseignant4.setDepartement("Génie Industriel");
        userService.createUser(enseignant4);

        // Étudiants
        User etudiant1 = new User();
        etudiant1.setNom("Ben Ali");
        etudiant1.setPrenom("Ahmed");
        etudiant1.setEmail("ahmed.benali@enicarthage.tn");
        etudiant1.setPassword("etudiant123");
        etudiant1.setRoles(List.of(User.Role.ETUDIANT));
        etudiant1.setDepartement("Informatique");
        userService.createUser(etudiant1);

        User etudiant2 = new User();
        etudiant2.setNom("Trabelsi");
        etudiant2.setPrenom("Nour");
        etudiant2.setEmail("nour.trabelsi@enicarthage.tn");
        etudiant2.setPassword("etudiant123");
        etudiant2.setRoles(List.of(User.Role.ETUDIANT));
        etudiant2.setDepartement("Mécatronique");
        userService.createUser(etudiant2);

        log.info("✅ {} utilisateurs créés", userService.countUsers());
    }

    private void createSalles() {
        log.info("📝 Création des salles ENICarthage...");

        // ============================================================
        // BÂTIMENT ANNEXE — Section Informatique
        // ============================================================

        // --- RDC (Rez-de-chaussée) ---
        Salle mac = new Salle();
        mac.setNom("Salle MAC");
        mac.setCapacite(30);
        mac.setEquipements(Arrays.asList("Ordinateurs Apple Mac", "Projecteur", "Tableau interactif", "WiFi"));
        mac.setBatiment("Bâtiment Annexe");
        mac.setEtage(0);
        mac.setDisponible(true);
        salleService.createSalle(mac);

        Salle labo = new Salle();
        labo.setNom("LABO Informatique");
        labo.setCapacite(25);
        labo.setEquipements(Arrays.asList("Ordinateurs", "Serveurs", "Switch réseau", "WiFi"));
        labo.setBatiment("Bâtiment Annexe");
        labo.setEtage(0);
        labo.setDisponible(true);
        salleService.createSalle(labo);

        Salle polyvalente = new Salle();
        polyvalente.setNom("Salle Polyvalente");
        polyvalente.setCapacite(150);
        polyvalente.setEquipements(Arrays.asList("Scène", "Système audio", "Projecteur HD", "Micro", "WiFi"));
        polyvalente.setBatiment("Bâtiment Annexe");
        polyvalente.setEtage(0);
        polyvalente.setDisponible(true);
        salleService.createSalle(polyvalente);

        // --- 1er Étage — Salles 20 à 27 ---
        int[] salles1erEtageAnnexe = {20, 21, 22, 23, 24, 25, 26, 27};
        for (int num : salles1erEtageAnnexe) {
            Salle salle = new Salle();
            salle.setNom("Salle " + num);
            salle.setCapacite(35);
            salle.setEquipements(Arrays.asList("Projecteur", "Tableau blanc", "WiFi"));
            salle.setBatiment("Bâtiment Annexe");
            salle.setEtage(1);
            salle.setDisponible(true);
            salleService.createSalle(salle);
        }

        // --- 2ème Étage — Salles 30 à 37 ---
        int[] salles2emeEtageAnnexe = {30, 31, 32, 33, 34, 35, 36, 37};
        for (int num : salles2emeEtageAnnexe) {
            Salle salle = new Salle();
            salle.setNom("Salle " + num);
            salle.setCapacite(35);
            salle.setEquipements(Arrays.asList("Projecteur", "Tableau blanc", "WiFi"));
            salle.setBatiment("Bâtiment Annexe");
            salle.setEtage(2);
            salle.setDisponible(true);
            salleService.createSalle(salle);
        }

        // ============================================================
        // BÂTIMENT PRINCIPAL — Section Mécatronique & Génie Industriel
        // ============================================================

        // --- RDC — Amphithéâtre ---
        Salle amphi = new Salle();
        amphi.setNom("Amphithéâtre Principal");
        amphi.setCapacite(250);
        amphi.setEquipements(Arrays.asList("Projecteur HD", "Micro", "Système audio", "Écran géant", "WiFi"));
        amphi.setBatiment("Bâtiment Principal");
        amphi.setEtage(0);
        amphi.setDisponible(true);
        salleService.createSalle(amphi);

        // --- 1er Étage — Salles 20 à 27 ---
        int[] salles1erEtagePrincipal = {20, 21, 22, 23, 24, 25, 26, 27};
        for (int num : salles1erEtagePrincipal) {
            Salle salle = new Salle();
            salle.setNom("Salle P" + num);   // Préfixe "P" pour distinguer du Bâtiment Annexe
            salle.setCapacite(35);
            salle.setEquipements(Arrays.asList("Projecteur", "Tableau blanc", "WiFi"));
            salle.setBatiment("Bâtiment Principal");
            salle.setEtage(1);
            salle.setDisponible(true);
            salleService.createSalle(salle);
        }

        // --- 2ème Étage — Salles 30 à 37 ---
        int[] salles2emeEtagePrincipal = {30, 31, 32, 33, 34, 35, 36, 37};
        for (int num : salles2emeEtagePrincipal) {
            Salle salle = new Salle();
            salle.setNom("Salle P" + num);
            salle.setCapacite(35);
            salle.setEquipements(Arrays.asList("Projecteur", "Tableau blanc", "WiFi"));
            salle.setBatiment("Bâtiment Principal");
            salle.setEtage(2);
            salle.setDisponible(true);
            salleService.createSalle(salle);
        }

        // --- 3ème Étage — Salles 40 à 47 ---
        int[] salles3emeEtagePrincipal = {40, 41, 42, 43, 44, 45, 46, 47};
        for (int num : salles3emeEtagePrincipal) {
            Salle salle = new Salle();
            salle.setNom("Salle P" + num);
            salle.setCapacite(35);
            salle.setEquipements(Arrays.asList("Projecteur", "Tableau blanc", "WiFi"));
            salle.setBatiment("Bâtiment Principal");
            salle.setEtage(3);
            salle.setDisponible(true);
            salleService.createSalle(salle);
        }

        log.info("✅ {} salles créées", salleService.countSalles());
    }

    private void createReservations() {
        log.info("📝 Création des réservations exemple...");

        User enseignant = userService.getUserByEmail("jean.dupont@enicarthage.tn")
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

        // Réserver la Salle MAC pour un TP
        Salle salleMac = salleService.getSallesByBatiment("Bâtiment Annexe")
                .stream()
                .filter(s -> s.getNom().equals("Salle MAC"))
                .findFirst()
                .orElseThrow();

        Reservation reservation1 = new Reservation();
        reservation1.setEnseignant(enseignant);
        reservation1.setSalle(salleMac);
        reservation1.setDateDebut(LocalDateTime.now().plusDays(3).withHour(10).withMinute(0));
        reservation1.setDateFin(LocalDateTime.now().plusDays(3).withHour(12).withMinute(0));
        reservation1.setMotif("TP Programmation Web — Groupe GL3");
        reservation1.setNombreParticipants(28);
        reservation1.setStatut(Reservation.Statut.APPROUVEE);
        reservationService.createReservation(reservation1);

        // Réserver le LABO pour un cours
        Salle salleLabo = salleService.getSallesByBatiment("Bâtiment Annexe")
                .stream()
                .filter(s -> s.getNom().equals("LABO Informatique"))
                .findFirst()
                .orElseThrow();

        Reservation reservation2 = new Reservation();
        reservation2.setEnseignant(enseignant);
        reservation2.setSalle(salleLabo);
        reservation2.setDateDebut(LocalDateTime.now().plusDays(7).withHour(14).withMinute(0));
        reservation2.setDateFin(LocalDateTime.now().plusDays(7).withHour(17).withMinute(0));
        reservation2.setMotif("TP Base de Données — Groupe RT2");
        reservation2.setNombreParticipants(22);
        reservation2.setStatut(Reservation.Statut.EN_ATTENTE);
        reservationService.createReservation(reservation2);

        // Enseignant mécatronique réserve une salle du Bâtiment Principal
        User enseignantMeca = userService.getUserByEmail("sophie.martin@enicarthage.tn")
                .orElseThrow(() -> new RuntimeException("Enseignant Mécatronique non trouvé"));

        Salle salleP20 = salleService.getSallesByBatiment("Bâtiment Principal")
                .stream()
                .filter(s -> s.getNom().equals("Salle P20"))
                .findFirst()
                .orElseThrow();

        Reservation reservation3 = new Reservation();
        reservation3.setEnseignant(enseignantMeca);
        reservation3.setSalle(salleP20);
        reservation3.setDateDebut(LocalDateTime.now().plusDays(2).withHour(8).withMinute(0));
        reservation3.setDateFin(LocalDateTime.now().plusDays(2).withHour(10).withMinute(0));
        reservation3.setMotif("Cours Automatique Industrielle — Groupe MI2");
        reservation3.setNombreParticipants(30);
        reservation3.setStatut(Reservation.Statut.APPROUVEE);
        reservationService.createReservation(reservation3);

        log.info("✅ {} réservations créées", reservationService.countReservations());
    }

    private void createEvents() {
        log.info("📝 Création des événements...");

        User admin = userService.getUserByEmail("admin@enicarthage.tn")
                .orElseThrow(() -> new RuntimeException("Admin non trouvé"));

        // Événement Salle Polyvalente (Bâtiment Annexe)
        Evenement event1 = new Evenement();
        event1.setTitre("Welcome Day ENICarthage 2025");
        event1.setDescription("Journée d'accueil pour les nouveaux étudiants — Toutes sections confondues.");
        event1.setDateDebut(LocalDateTime.now().plusDays(10).withHour(9).withMinute(0));
        event1.setDateFin(LocalDateTime.now().plusDays(10).withHour(17).withMinute(0));
        event1.setLieu("Salle Polyvalente — Bâtiment Annexe");
        event1.setNbParticipantsMax(150);
        event1.setOrganisateur(admin);
        event1.setStatut(Evenement.Statut.OUVERT);
        evenementService.createEvenement(event1);

        // Événement Amphithéâtre (Bâtiment Principal)
        Evenement event2 = new Evenement();
        event2.setTitre("Journée Portes Ouvertes ENICarthage");
        event2.setDescription("Présentation des filières Informatique, Mécatronique et Génie Industriel aux futurs étudiants.");
        event2.setDateDebut(LocalDateTime.now().plusDays(15).withHour(9).withMinute(0));
        event2.setDateFin(LocalDateTime.now().plusDays(15).withHour(17).withMinute(0));
        event2.setLieu("Amphithéâtre Principal — Bâtiment Principal");
        event2.setNbParticipantsMax(250);
        event2.setOrganisateur(admin);
        event2.setStatut(Evenement.Statut.OUVERT);
        evenementService.createEvenement(event2);

        // Hackathon Salle MAC
        Evenement event3 = new Evenement();
        event3.setTitre("Hackathon CampusHub — 24h pour innover");
        event3.setDescription("Compétition de développement logiciel pour améliorer la vie étudiante à ENICarthage.");
        event3.setDateDebut(LocalDateTime.now().plusWeeks(2).withHour(18).withMinute(0));
        event3.setDateFin(LocalDateTime.now().plusWeeks(2).plusDays(1).withHour(18).withMinute(0));
        event3.setLieu("Salle MAC — Bâtiment Annexe");
        event3.setNbParticipantsMax(30);
        event3.setOrganisateur(admin);
        event3.setStatut(Evenement.Statut.OUVERT);
        evenementService.createEvenement(event3);

        log.info("✅ {} événements créés", evenementService.countEvenements());
    }

    private void createDemandes() {
        log.info("📝 Création des demandes de documents...");

        User etudiant = userService.getUserByEmail("ahmed.benali@enicarthage.tn")
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        DemandeDocument demande1 = new DemandeDocument();
        demande1.setTypeDocument(DemandeDocument.TypeDocument.CERTIFICAT_SCOLARITE);
        demande1.setDemandeur(etudiant);
        demande1.setStatut(DemandeDocument.Statut.PRET);
        demande1.setCommentaireAdmin("Votre certificat est prêt à être récupéré au bureau d'ordre.");
        demandeDocumentService.createDemande(demande1);

        DemandeDocument demande2 = new DemandeDocument();
        demande2.setTypeDocument(DemandeDocument.TypeDocument.RELEVE_NOTES);
        demande2.setDemandeur(etudiant);
        demande2.setStatut(DemandeDocument.Statut.EN_ATTENTE);
        demandeDocumentService.createDemande(demande2);

        User etudiant2 = userService.getUserByEmail("nour.trabelsi@enicarthage.tn")
                .orElseThrow(() -> new RuntimeException("Étudiant 2 non trouvé"));

        DemandeDocument demande3 = new DemandeDocument();
        demande3.setTypeDocument(DemandeDocument.TypeDocument.ATTESTATION_PRESENCE);
        demande3.setDemandeur(etudiant2);
        demande3.setStatut(DemandeDocument.Statut.EN_COURS);
        demandeDocumentService.createDemande(demande3);

        log.info("✅ {} demandes de documents créées", demandeDocumentService.countDemandes());
    }
}