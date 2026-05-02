package tn.enicarthage.campushub.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.enicarthage.campushub.admin.repository.ClubRepository;
import tn.enicarthage.campushub.enseignant.model.DemandeDocument;
import tn.enicarthage.campushub.enseignant.model.Evenement;
import tn.enicarthage.campushub.enseignant.model.Reservation;
import tn.enicarthage.campushub.enseignant.model.Salle;
import tn.enicarthage.campushub.enseignant.service.DemandeDocumentService;
import tn.enicarthage.campushub.enseignant.service.EvenementService;
import tn.enicarthage.campushub.enseignant.service.ReservationService;
import tn.enicarthage.campushub.enseignant.service.SalleService;
import tn.enicarthage.campushub.shared.model.Club;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.service.UserService;
import tn.enicarthage.campushub.student.model.ApplicationStatus;
import tn.enicarthage.campushub.student.model.ClubApplication;
import tn.enicarthage.campushub.student.model.EventRegistration;
import tn.enicarthage.campushub.student.repository.ClubApplicationRepository;
import tn.enicarthage.campushub.student.repository.EventRegistrationRepository;

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
    private final ClubRepository clubRepository;
    private final ClubApplicationRepository clubApplicationRepository;
    private final EventRegistrationRepository eventRegistrationRepository;

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
            createClubs();
            log.info("✅ Initialisation des données terminée avec succès !");
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'initialisation des données", e);
        }
    }

    // =========================================================================
    // USERS
    // =========================================================================

    private void createUsers() {
        log.info("📝 Création des utilisateurs...");

        // --- Admin ---
        createUser("Admin", "System", "admin@enicarthage.tn", "admin123",
                List.of(User.Role.ADMIN), "Administration");

        // --- Enseignants ---
        createUser("Dupont", "Jean", "jean.dupont@enicarthage.tn", "enseignant123",
                List.of(User.Role.ENSEIGNANT), "Informatique");

        createUser("Ben Salah", "Sarra", "sarra.bensalah@enicarthage.tn", "enseignant123",
                List.of(User.Role.ENSEIGNANT), "Informatique");

        createUser("Martin", "Sophie", "sophie.martin@enicarthage.tn", "enseignant123",
                List.of(User.Role.ENSEIGNANT), "Mécatronique");

        createUser("Gharbi", "Karim", "karim.gharbi@enicarthage.tn", "enseignant123",
                List.of(User.Role.ENSEIGNANT), "Génie Industriel");

        // --- Students — Happy Path ---
        // Standard Informatique student — used for most demande/event tests
        createUser("Ben Ali", "Ahmed", "ahmed.benali@enicarthage.tn", "etudiant123",
                List.of(User.Role.ETUDIANT), "Informatique");

        // Standard Mécatronique student
        createUser("Trabelsi", "Nour", "nour.trabelsi@enicarthage.tn", "etudiant123",
                List.of(User.Role.ETUDIANT), "Mécatronique");

        // --- Students — Edge Cases ---

        // Multi-role: ETUDIANT + RESPONSABLE_CLUB (club head scenario)
        createUser("Jaziri", "Omar", "omar.jaziri@enicarthage.tn", "etudiant123",
                List.of(User.Role.ETUDIANT, User.Role.RESPONSABLE_CLUB), "Informatique");

        // Triple-role: ETUDIANT + RESPONSABLE_CLUB + ENSEIGNANT (assistant / monitor hybrid)
        createUser("Sfar", "Lina", "lina.sfar@enicarthage.tn", "multi123",
                List.of(User.Role.ETUDIANT, User.Role.RESPONSABLE_CLUB),
                "Informatique");

        // Génie Industriel student — tests department filtering if ever added
        createUser("Msaada", "Yassine", "yassine.msaada@enicarthage.tn", "etudiant123",
                List.of(User.Role.ETUDIANT), "Génie Industriel");

        // Student with no activity — clean slate for isolation tests
        createUser("Ferchichi", "Rim", "rim.ferchichi@enicarthage.tn", "etudiant123",
                List.of(User.Role.ETUDIANT), "Mécatronique");

        log.info("✅ {} utilisateurs créés", userService.countUsers());
    }

    private void createUser(String nom, String prenom, String email, String password,
                            List<User.Role> roles, String departement) {
        User u = new User();
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setEmail(email);
        u.setPassword(password);
        u.setRoles(roles);
        u.setDepartement(departement);
        userService.createUser(u);
    }

    // =========================================================================
    // SALLES  (unchanged — already well structured)
    // =========================================================================

    private void createSalles() {
        log.info("📝 Création des salles ENICarthage...");

        // --- Bâtiment Annexe — RDC ---
        createSalle("Salle MAC", 30,
                List.of("Ordinateurs Apple Mac", "Projecteur", "Tableau interactif", "WiFi"),
                "Bâtiment Annexe", 0);

        createSalle("LABO Informatique", 25,
                List.of("Ordinateurs", "Serveurs", "Switch réseau", "WiFi"),
                "Bâtiment Annexe", 0);

        createSalle("Salle Polyvalente", 150,
                List.of("Scène", "Système audio", "Projecteur HD", "Micro", "WiFi"),
                "Bâtiment Annexe", 0);

        // --- Bâtiment Annexe — Étages 1 & 2 ---
        for (int n : new int[]{20, 21, 22, 23, 24, 25, 26, 27})
            createSalle("Salle " + n, 35, List.of("Projecteur", "Tableau blanc", "WiFi"), "Bâtiment Annexe", 1);

        for (int n : new int[]{30, 31, 32, 33, 34, 35, 36, 37})
            createSalle("Salle " + n, 35, List.of("Projecteur", "Tableau blanc", "WiFi"), "Bâtiment Annexe", 2);

        // --- Bâtiment Principal — RDC ---
        createSalle("Amphithéâtre Principal", 250,
                List.of("Projecteur HD", "Micro", "Système audio", "Écran géant", "WiFi"),
                "Bâtiment Principal", 0);

        // --- Bâtiment Principal — Étages 1, 2 & 3 ---
        for (int n : new int[]{20, 21, 22, 23, 24, 25, 26, 27})
            createSalle("Salle P" + n, 35, List.of("Projecteur", "Tableau blanc", "WiFi"), "Bâtiment Principal", 1);

        for (int n : new int[]{30, 31, 32, 33, 34, 35, 36, 37})
            createSalle("Salle P" + n, 35, List.of("Projecteur", "Tableau blanc", "WiFi"), "Bâtiment Principal", 2);

        for (int n : new int[]{40, 41, 42, 43, 44, 45, 46, 47})
            createSalle("Salle P" + n, 35, List.of("Projecteur", "Tableau blanc", "WiFi"), "Bâtiment Principal", 3);

        log.info("✅ {} salles créées", salleService.countSalles());
    }

    private void createSalle(String nom, int capacite, List<String> equipements,
                             String batiment, int etage) {
        Salle s = new Salle();
        s.setNom(nom);
        s.setCapacite(capacite);
        s.setEquipements(equipements);
        s.setBatiment(batiment);
        s.setEtage(etage);
        s.setDisponible(true);
        salleService.createSalle(s);
    }

    // =========================================================================
    // RESERVATIONS
    // =========================================================================

    private void createReservations() {
        log.info("📝 Création des réservations exemple...");

        User jean = findUser("jean.dupont@enicarthage.tn");
        User sophie = findUser("sophie.martin@enicarthage.tn");
        User sarra = findUser("sarra.bensalah@enicarthage.tn");

        Salle mac   = findSalleByName("Bâtiment Annexe", "Salle MAC");
        Salle labo  = findSalleByName("Bâtiment Annexe", "LABO Informatique");
        Salle p20   = findSalleByName("Bâtiment Principal", "Salle P20");
        Salle s21   = findSalleByName("Bâtiment Annexe", "Salle 21");

        // Future — APPROUVEE
        createReservation(jean, mac,
                daysFromNow(3, 10, 0), daysFromNow(3, 12, 0),
                "TP Programmation Web — Groupe GL3", 28,
                Reservation.Statut.APPROUVEE);

        // Future — EN_ATTENTE
        createReservation(jean, labo,
                daysFromNow(7, 14, 0), daysFromNow(7, 17, 0),
                "TP Base de Données — Groupe RT2", 22,
                Reservation.Statut.EN_ATTENTE);

        // Future — APPROUVEE (Mécatronique)
        createReservation(sophie, p20,
                daysFromNow(2, 8, 0), daysFromNow(2, 10, 0),
                "Cours Automatique Industrielle — Groupe MI2", 30,
                Reservation.Statut.APPROUVEE);

        // Past — APPROUVEE (completed, useful for history views)
        createReservation(sarra, s21,
                daysFromNow(-5, 9, 0), daysFromNow(-5, 11, 0),
                "TP Algorithmes — Groupe GL2", 30,
                Reservation.Statut.APPROUVEE);

        // Future — REFUSEE (rejection scenario)
        createReservation(sarra, mac,
                daysFromNow(4, 13, 0), daysFromNow(4, 15, 0),
                "Cours Réseaux — conflit d'horaire", 20,
                Reservation.Statut.REJETEE);

        log.info("✅ {} réservations créées", reservationService.countReservations());
    }

    private void createReservation(User enseignant, Salle salle,
                                   LocalDateTime debut, LocalDateTime fin,
                                   String motif, int participants,
                                   Reservation.Statut statut) {
        Reservation r = new Reservation();
        r.setEnseignant(enseignant);
        r.setSalle(salle);
        r.setDateDebut(debut);
        r.setDateFin(fin);
        r.setMotif(motif);
        r.setNombreParticipants(participants);
        r.setStatut(statut);
        reservationService.createReservation(r);
    }

    // =========================================================================
    // EVENTS  — full lifecycle coverage
    // =========================================================================

    private void createEvents() {
        log.info("📝 Création des événements...");

        User admin  = findUser("admin@enicarthage.tn");
        User ahmed  = findUser("ahmed.benali@enicarthage.tn");
        User omar   = findUser("omar.jaziri@enicarthage.tn");

        // ── OUVERT — future, plenty of capacity ──────────────────────────────

        Evenement welcomeDay = createEvent(
                "Welcome Day ENICarthage 2025",
                "Journée d'accueil pour les nouveaux étudiants — Toutes sections confondues.",
                daysFromNow(10, 9, 0), daysFromNow(10, 17, 0),
                "Salle Polyvalente — Bâtiment Annexe",
                150, admin, Evenement.Statut.OUVERT);

        Evenement jpo = createEvent(
                "Journée Portes Ouvertes ENICarthage",
                "Présentation des filières Informatique, Mécatronique et Génie Industriel.",
                daysFromNow(15, 9, 0), daysFromNow(15, 17, 0),
                "Amphithéâtre Principal — Bâtiment Principal",
                250, admin, Evenement.Statut.OUVERT);

        Evenement workshop = createEvent(
                "Coding Workshop Spring Boot",
                "Atelier pratique Spring Boot pour étudiants — tous niveaux.",
                daysFromNow(3, 14, 0), daysFromNow(3, 17, 0),
                "Salle MAC — Bâtiment Annexe",
                50, admin, Evenement.Statut.OUVERT);

        // Student-submitted event (responsable de club) — also OUVERT
        Evenement clubNight = createEvent(
                "Club IT — Soirée Coding Challenge",
                "Compétition inter-clubs de programmation organisée par le Club Informatique.",
                daysFromNow(20, 18, 0), daysFromNow(20, 22, 0),
                "Salle MAC — Bâtiment Annexe",
                30, omar, Evenement.Statut.OUVERT);

        // ── PLEIN — capacity exactly met, no more registrations allowed ───────
        // capacity = 1 so it's trivially full after one seeded registration below
        Evenement seminaire = createEvent(
                "Séminaire IA & Robotique",
                "Conférence sur l'Intelligence Artificielle appliquée à la robotique industrielle.",
                daysFromNow(8, 10, 0), daysFromNow(8, 13, 0),
                "Amphithéâtre Principal — Bâtiment Principal",
                1, admin, Evenement.Statut.PLEIN);

        // ── TERMINE — past events that concluded normally ─────────────────────
        Evenement hackathon = createEvent(
                "Hackathon CampusHub — 24h pour innover",
                "Compétition de développement logiciel pour améliorer la vie étudiante.",
                daysFromNow(-14, 18, 0), daysFromNow(-13, 18, 0),
                "Salle MAC — Bâtiment Annexe",
                30, admin, Evenement.Statut.TERMINE);

        Evenement forumEmploi = createEvent(
                "Forum Emploi & Stages ENICarthage",
                "Rencontre avec des entreprises partenaires pour stages et emplois.",
                daysFromNow(-7, 9, 0), daysFromNow(-7, 17, 0),
                "Salle Polyvalente — Bâtiment Annexe",
                120, admin, Evenement.Statut.TERMINE);

        // ── ANNULE — cancelled before it happened ─────────────────────────────
        Evenement sortieCulture = createEvent(
                "Sortie Culturelle — Carthage by Night",
                "Visite nocturne des ruines de Carthage pour les étudiants ENI.",
                daysFromNow(5, 19, 0), daysFromNow(5, 23, 0),
                "Départ ENICarthage", 60, admin, Evenement.Statut.ANNULE);

        // ── REJETE — submitted by a student, rejected by admin ───────────────
        Evenement concertRejete = createEvent(
                "Concert Acoustique Campus",
                "Soirée musicale organisée par des étudiants — demande rejetée faute de salle.",
                daysFromNow(12, 20, 0), daysFromNow(12, 23, 0),
                "Salle Polyvalente — Bâtiment Annexe",
                100, ahmed, Evenement.Statut.REJETE);
        // A rejected event should carry an admin comment explaining why
        concertRejete.setCommentaireAdmin("Salle Polyvalente déjà réservée pour cette date.");
        evenementService.createEvenement(concertRejete); // re-save not needed if service saves; see note below

        // ── EN_ATTENTE — submitted, awaiting admin approval ───────────────────
        createEvent(
                "Atelier Design Thinking",
                "Atelier collaboratif pour imaginer de nouveaux services étudiants.",
                daysFromNow(25, 9, 0), daysFromNow(25, 12, 0),
                "Salle 22 — Bâtiment Annexe",
                25, omar, Evenement.Statut.EN_ATTENTE);

        // ── Seed EventRegistrations ────────────────────────────────────────────
        // ahmed is registered for workshop and welcomeDay
        seedRegistration(ahmed.getId(), workshop.getId());
        seedRegistration(ahmed.getId(), welcomeDay.getId());
        // nour is registered for jpo
        User nour = findUser("nour.trabelsi@enicarthage.tn");
        seedRegistration(nour.getId(), jpo.getId());
        // seminaire is PLEIN — the one seat is taken by yassine
        User yassine = findUser("yassine.msaada@enicarthage.tn");
        seedRegistration(yassine.getId(), seminaire.getId());
        // omar is registered for hackathon (past, TERMINE)
        seedRegistration(omar.getId(), hackathon.getId());

        log.info("✅ {} événements créés", evenementService.countEvenements());
    }

    private Evenement createEvent(String titre, String description,
                                  LocalDateTime debut, LocalDateTime fin,
                                  String lieu, int capacite,
                                  User organisateur, Evenement.Statut statut) {
        Evenement e = new Evenement();
        e.setTitre(titre);
        e.setDescription(description);
        e.setDateDebut(debut);
        e.setDateFin(fin);
        e.setLieu(lieu);
        e.setNbParticipantsMax(capacite);
        e.setOrganisateur(organisateur);
        e.setStatut(statut);
        return evenementService.createEvenement(e);
    }

    private void seedRegistration(Long studentId, Long eventId) {
        EventRegistration reg = new EventRegistration();
        reg.setStudentId(studentId);
        reg.setEventId(eventId);
        eventRegistrationRepository.save(reg);
    }

    // =========================================================================
    // DEMANDES DE DOCUMENTS — all 5 statuses, multiple students, edge cases
    // =========================================================================

    private void createDemandes() {
        log.info("📝 Création des demandes de documents...");

        User ahmed   = findUser("ahmed.benali@enicarthage.tn");
        User nour    = findUser("nour.trabelsi@enicarthage.tn");
        User yassine = findUser("yassine.msaada@enicarthage.tn");
        User omar    = findUser("omar.jaziri@enicarthage.tn");

        // ── Ahmed — covers all 5 statuses ────────────────────────────────────
        createDemande(ahmed, DemandeDocument.TypeDocument.RELEVE_NOTES,
                DemandeDocument.Statut.EN_ATTENTE, null);

        createDemande(ahmed, DemandeDocument.TypeDocument.CERTIFICAT_SCOLARITE,
                DemandeDocument.Statut.EN_COURS, "Document en cours de traitement par le secrétariat.");

        createDemande(ahmed, DemandeDocument.TypeDocument.ATTESTATION_PRESENCE,
                DemandeDocument.Statut.PRET, "Document prêt — à retirer au secrétariat avant vendredi.");

        createDemande(ahmed, DemandeDocument.TypeDocument.CARTE_ETUDIANT,
                DemandeDocument.Statut.REJETE, "Pièce justificative manquante — veuillez fournir une photo d'identité.");

        createDemande(ahmed, DemandeDocument.TypeDocument.AUTRE,
                DemandeDocument.Statut.RECUPERE, "Document récupéré le 15/04/2025.");

        // ── Edge case: duplicate type (same student, same type, different statuses) ──
        // Simulates a student who re-requested a RELEVE_NOTES after a first was REJETE
        createDemande(ahmed, DemandeDocument.TypeDocument.RELEVE_NOTES,
                DemandeDocument.Statut.EN_ATTENTE, "Deuxième demande suite au rejet de la première.");

        // ── Nour — EN_COURS + PRET (partial lifecycle) ───────────────────────
        createDemande(nour, DemandeDocument.TypeDocument.ATTESTATION_PRESENCE,
                DemandeDocument.Statut.EN_COURS, null);

        createDemande(nour, DemandeDocument.TypeDocument.CERTIFICAT_SCOLARITE,
                DemandeDocument.Statut.PRET, "Prêt à retirer.");

        // ── Yassine — single pending request ─────────────────────────────────
        createDemande(yassine, DemandeDocument.TypeDocument.RELEVE_NOTES,
                DemandeDocument.Statut.EN_ATTENTE, null);

        // ── Omar (multi-role) — REJETE scenario ──────────────────────────────
        createDemande(omar, DemandeDocument.TypeDocument.CARTE_ETUDIANT,
                DemandeDocument.Statut.REJETE, "Carte déjà émise ce semestre.");

        // ── rim.ferchichi — zero demandes (clean slate for isolation tests) ──
        // No demandes created intentionally

        log.info("✅ {} demandes de documents créées", demandeDocumentService.countDemandes());
    }

    private void createDemande(User demandeur, DemandeDocument.TypeDocument type,
                               DemandeDocument.Statut statut, String commentaire) {
        DemandeDocument d = new DemandeDocument();
        d.setDemandeur(demandeur);
        d.setTypeDocument(type);
        d.setStatut(statut);
        d.setCommentaireAdmin(commentaire);
        demandeDocumentService.createDemande(d);
    }

    // =========================================================================
    // CLUBS — 3 clubs covering all statuses + ClubApplications
    // =========================================================================

    private void createClubs() {
        log.info("📝 Création des clubs et candidatures...");

        User omar    = findUser("omar.jaziri@enicarthage.tn");
        User lina    = findUser("lina.sfar@enicarthage.tn");
        User ahmed   = findUser("ahmed.benali@enicarthage.tn");
        User nour    = findUser("nour.trabelsi@enicarthage.tn");
        User yassine = findUser("yassine.msaada@enicarthage.tn");

        // ACTIF — main club with members
        Club clubInfo = new Club();
        clubInfo.setNom("Club Informatique ENI");
        clubInfo.setDescription("Club dédié aux projets logiciels, hackathons et veille technologique.");
        clubInfo.setCategorie("Tech");
        clubInfo.setResponsable("Omar Jaziri");
        clubInfo.setNombreMembres(12);
        clubInfo.setHeadId(omar.getId());
        clubInfo.setStatut(Club.Statut.ACTIF);
        Club savedClubInfo = clubRepository.save(clubInfo);

        // EN_ATTENTE — new club waiting for admin approval
        Club clubRobo = new Club();
        clubRobo.setNom("Club Robotique & IA");
        clubRobo.setDescription("Club orienté robotique, intelligence artificielle et systèmes embarqués.");
        clubRobo.setCategorie("Tech");
        clubRobo.setResponsable("Lina Sfar");
        clubRobo.setNombreMembres(0);
        clubRobo.setHeadId(lina.getId());
        clubRobo.setStatut(Club.Statut.EN_ATTENTE);
        Club savedClubRobo = clubRepository.save(clubRobo);

        // SUSPENDU — example of a suspended club
        Club clubCulture = new Club();
        clubCulture.setNom("Club Culturel & Arts");
        clubCulture.setDescription("Club d'expression artistique — suspendu pour inactivité.");
        clubCulture.setCategorie("Culture");
        clubCulture.setResponsable("Non assigné");
        clubCulture.setNombreMembres(3);
        clubCulture.setStatut(Club.Statut.SUSPENDU);
        Club savedClubCulture = clubRepository.save(clubCulture);

        // ── ClubApplications ──────────────────────────────────────────────────

        // ahmed — ACCEPTED into Club Informatique
        saveApplication(ahmed.getId(), savedClubInfo.getId(),
                ApplicationStatus.ACCEPTED, "Passionné de développement web et mobile.");

        // nour — PENDING for Club Informatique
        saveApplication(nour.getId(), savedClubInfo.getId(),
                ApplicationStatus.PENDING, "Intéressée par les projets IoT et embarqué.");

        // yassine — PENDING for Club Robotique (new club, not yet approved)
        saveApplication(yassine.getId(), savedClubRobo.getId(),
                ApplicationStatus.PENDING, "Étudiant en GI, passionné de robotique.");

        // ahmed — REJECTED from Club Culture (edge case: rejection path)
        saveApplication(ahmed.getId(), savedClubCulture.getId(),
                ApplicationStatus.REJECTED, "Demande faite par erreur.");

        log.info("✅ {} clubs créés, {} candidatures créées",
                clubRepository.count(), clubApplicationRepository.count());
    }

    private void saveApplication(Long studentId, Long clubId,
                                 ApplicationStatus status, String motivation) {
        ClubApplication app = new ClubApplication();
        app.setStudentId(studentId);
        app.setClubId(clubId);
        app.setStatus(status);
        app.setMotivation(motivation);
        clubApplicationRepository.save(app);
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private User findUser(String email) {
        return userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + email));
    }

    private Salle findSalleByName(String batiment, String nom) {
        return salleService.getSallesByBatiment(batiment).stream()
                .filter(s -> s.getNom().equals(nom))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Salle introuvable : " + nom));
    }

    /** Returns a LocalDateTime N days from now, at the given hour:minute. */
    private LocalDateTime daysFromNow(int days, int hour, int minute) {
        return LocalDateTime.now().plusDays(days).withHour(hour).withMinute(minute).withSecond(0).withNano(0);
    }
}