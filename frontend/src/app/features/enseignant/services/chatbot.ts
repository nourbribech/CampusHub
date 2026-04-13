import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { ReservationService, Salle } from './reservation';

export interface Message {
  id: string;
  texte: string;
  auteur: 'user' | 'bot';
  timestamp: Date;
  actions?: Action[];
}

export interface Action {
  label: string;
  type: 'link' | 'function';
  data?: any;
}

@Injectable({
  providedIn: 'root'
})
export class Chatbot {
  private messagesSubject = new BehaviorSubject<Message[]>([]);
  public messages$ = this.messagesSubject.asObservable();

  private conversationState: ConversationState = {
    etape: 'accueil',
    data: {}
  };

  private salles: Salle[] = [];

  constructor(
    private reservationService: ReservationService,
    private router: Router
  ) {
    this.chargerSalles();
    this.envoyerMessageBot(
      "👋 Bonjour ! Je suis **Claude**, votre assistant virtuel ENICarthage.\n\n" +
      "Comment puis-je vous aider aujourd'hui ?",
      [
        { label: '📅 Réserver une salle', type: 'function', data: 'reserver' },
        { label: '📋 Voir mes réservations', type: 'link', data: '/enseignant/mes-reservations' },
        { label: '📅 Calendrier', type: 'link', data: '/enseignant/calendrier' },
        { label: '❓ Aide', type: 'function', data: 'aide' }
      ]
    );
  }

  chargerSalles(): void {
    this.reservationService.getSalles().subscribe({
      next: (salles) => {
        this.salles = salles;
      }
    });
  }

  envoyerMessageUtilisateur(texte: string): void {
    const message: Message = {
      id: this.genererID(),
      texte,
      auteur: 'user',
      timestamp: new Date()
    };

    this.ajouterMessage(message);
    this.traiterMessage(texte);
  }

  private traiterMessage(texte: string): void {
    const texteLower = texte.toLowerCase().trim();

    // Réservation guidée
    if (this.conversationState.etape === 'reservation_batiment') {
      this.traiterChoixBatiment(texte);
      return;
    }

    if (this.conversationState.etape === 'reservation_date') {
      this.traiterChoixDate(texte);
      return;
    }

    if (this.conversationState.etape === 'reservation_heure') {
      this.traiterChoixHeure(texte);
      return;
    }

    if (this.conversationState.etape === 'reservation_salle') {
      this.traiterChoixSalle(texte);
      return;
    }

    if (this.conversationState.etape === 'reservation_motif') {
      this.traiterChoixMotif(texte);
      return;
    }

    // Détection d'intentions (mots-clés)
    if (this.detecterIntention(texteLower, ['réserver', 'reservation', 'salle', 'reserver'])) {
      this.demarrerReservation();
    }
    else if (this.detecterIntention(texteLower, ['aide', 'help', 'comment', 'utiliser'])) {
      this.afficherAide();
    }
    else if (this.detecterIntention(texteLower, ['mes reservations', 'voir', 'liste', 'historique'])) {
      this.afficherMesReservations();
    }
    else if (this.detecterIntention(texteLower, ['calendrier', 'planning', 'agenda'])) {
      this.afficherCalendrier();
    }
    else if (this.detecterIntention(texteLower, ['disponible', 'dispo', 'libre', 'maintenant'])) {
      this.afficherSallesDisponibles();
    }
    else if (this.detecterIntention(texteLower, ['annexe', 'informatique'])) {
      this.afficherSallesAnnexe();
    }
    else if (this.detecterIntention(texteLower, ['principal', 'mecatronique', 'mécatronique', 'industriel'])) {
      this.afficherSallesPrincipal();
    }
    else if (this.detecterIntention(texteLower, ['amphi', 'amphitheatre', 'amphithéâtre'])) {
      this.afficherInfoAmphi();
    }
    else if (this.detecterIntention(texteLower, ['labo', 'laboratoire'])) {
      this.afficherInfoLabo();
    }
    else if (this.detecterIntention(texteLower, ['merci', 'thanks', 'super', 'parfait', 'ok'])) {
      this.repondreGentiment();
    }
    else if (this.detecterIntention(texteLower, ['bonjour', 'salut', 'hello', 'hey'])) {
      this.saluer();
    }
    else {
      this.repondrePasCompris();
    }
  }

  private detecterIntention(texte: string, motsClés: string[]): boolean {
    return motsClés.some(mot => texte.includes(mot));
  }

  // ========== ACTIONS DU CHATBOT ==========

  private demarrerReservation(): void {
    this.conversationState.etape = 'reservation_batiment';
    this.conversationState.data = {};

    this.envoyerMessageBot(
      "Parfait ! Je vais vous guider pour réserver une salle. 🎯\n\n" +
      "**Étape 1/4 :** Quel bâtiment préférez-vous ?",
      [
        { label: '💻 Bâtiment Annexe (Informatique)', type: 'function', data: 'annexe' },
        { label: '🏭 Bâtiment Principal (Mécatronique)', type: 'function', data: 'principal' },
        { label: '🏫 Peu importe', type: 'function', data: 'tous' }
      ]
    );
  }

  private traiterChoixBatiment(choix: string): void {
    this.conversationState.data.batiment = choix;
    this.conversationState.etape = 'reservation_date';

    const batimentText = choix === 'annexe' ? 'Bâtiment Annexe' :
      choix === 'principal' ? 'Bâtiment Principal' :
        'tous les bâtiments';

    this.envoyerMessageBot(
      `Super ! Vous avez choisi : **${batimentText}**\n\n` +
      "**Étape 2/4 :** Pour quelle date souhaitez-vous réserver ?\n\n" +
      "💡 *Tapez une date (ex: demain, lundi, 15/04/2026) ou choisissez :*",
      [
        { label: '📅 Aujourd\'hui', type: 'function', data: 'aujourdhui' },
        { label: '📅 Demain', type: 'function', data: 'demain' },
        { label: '📅 Lundi prochain', type: 'function', data: 'lundi' }
      ]
    );
  }

  private traiterChoixDate(choix: string): void {
    let date = new Date();

    if (choix === 'demain') {
      date.setDate(date.getDate() + 1);
    } else if (choix === 'lundi') {
      const jourActuel = date.getDay();
      const joursJusquaLundi = jourActuel === 0 ? 1 : 8 - jourActuel;
      date.setDate(date.getDate() + joursJusquaLundi);
    }
    // Sinon on garde aujourd'hui

    this.conversationState.data.date = date;
    this.conversationState.etape = 'reservation_heure';

    const dateFormatee = date.toLocaleDateString('fr-FR', {
      weekday: 'long',
      day: 'numeric',
      month: 'long'
    });

    this.envoyerMessageBot(
      `Date sélectionnée : **${dateFormatee}**\n\n` +
      "**Étape 3/4 :** Quel créneau horaire ?",
      [
        { label: '🌅 8h - 10h', type: 'function', data: '8-10' },
        { label: '☀️ 10h - 12h', type: 'function', data: '10-12' },
        { label: '🌤️ 14h - 16h', type: 'function', data: '14-16' },
        { label: '🌆 16h - 18h', type: 'function', data: '16-18' }
      ]
    );
  }

  private traiterChoixHeure(choix: string): void {
    const [heureDebut, heureFin] = choix.split('-').map(Number);

    this.conversationState.data.heureDebut = heureDebut;
    this.conversationState.data.heureFin = heureFin;
    this.conversationState.etape = 'reservation_salle';

    // Filtrer les salles disponibles
    const batiment = this.conversationState.data.batiment;
    let sallesDisponibles = this.salles;

    if (batiment === 'annexe') {
      sallesDisponibles = this.salles.filter(s => s.batiment === 'Bâtiment Annexe');
    } else if (batiment === 'principal') {
      sallesDisponibles = this.salles.filter(s => s.batiment === 'Bâtiment Principal');
    }

    // Prendre les 5 premières
    const suggestions = sallesDisponibles.slice(0, 5);

    this.envoyerMessageBot(
      `Créneau : **${heureDebut}h - ${heureFin}h**\n\n` +
      "**Étape 4/4 :** Voici les salles disponibles :\n\n" +
      suggestions.map((s, i) =>
        `${i + 1}. **${s.nom}** (${s.capacite} places) - ${s.batiment}`
      ).join('\n') +
      "\n\n💡 *Tapez le numéro de la salle ou son nom*",
      [
        { label: '🔗 Voir toutes les salles', type: 'link', data: '/enseignant/reservation-salle' }
      ]
    );

    this.conversationState.data.sallesDisponibles = suggestions;
  }

  private traiterChoixSalle(choix: string): void {
    const suggestions: Salle[] = this.conversationState.data.sallesDisponibles || [];
    let salle: Salle | undefined;

    // Vérifier si c'est un numéro
    const numero = parseInt(choix);
    if (!isNaN(numero) && numero >= 1 && numero <= suggestions.length) {
      salle = suggestions[numero - 1];
    } else {
      // Chercher par nom
      salle = suggestions.find(s =>
        s.nom.toLowerCase().includes(choix.toLowerCase())
      );
    }

    if (salle) {
      this.conversationState.data.salleSelectionnee = salle;
      this.conversationState.etape = 'reservation_motif';

      this.envoyerMessageBot(
        `📍 Vous avez choisi la salle **${salle.nom}**.\n\n` +
        "Dernière étape : quel est le **motif** de votre réservation ?\n" +
        "💡 *Ex: CM de Développement Web, TP Algorithmique, Réunion...*"
      );
    } else {
      this.envoyerMessageBot(
        "❌ Je n'ai pas trouvé cette salle. Pouvez-vous réessayer ?\n\n" +
        "💡 *Tapez le numéro (1, 2, 3...) ou le nom de la salle*"
      );
    }
  }

  private traiterChoixMotif(texte: string): void {
    if (texte.length < 3) {
      this.envoyerMessageBot("Veuillez donner un motif un peu plus explicite pour votre réservation.");
      return;
    }

    this.conversationState.data.motif = texte;
    const data = this.conversationState.data;
    const salle = data.salleSelectionnee!;

    this.conversationState.etape = 'accueil'; // On revient à l'accueil pour finir

    this.envoyerMessageBot(
      `✅ **Récapitulatif de votre réservation :**\n\n` +
      `🏢 **Salle :** ${salle.nom} (${salle.batiment})\n` +
      `📅 **Date :** ${data.date?.toLocaleDateString('fr-FR')}\n` +
      `🕐 **Horaire :** ${data.heureDebut}h - ${data.heureFin}h\n` +
      `📝 **Motif :** ${texte}\n\n` +
      "Voulez-vous confirmer cette réservation ?",
      [
        { label: '✅ Confirmer et enregistrer', type: 'function', data: 'finaliser' },
        { label: '🔄 Recommencer', type: 'function', data: 'reserver' }
      ]
    );
  }

  private finaliserReservation(): void {
    const data = this.conversationState.data;
    if (!data.salleSelectionnee || !data.date || !data.heureDebut || !data.heureFin || !data.motif) {
      this.envoyerMessageBot("⚠️ Désolé, il manque des informations pour finaliser la réservation.");
      return;
    }

    // Préparation des dates ISO
    const date = data.date;
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    const dateStr = `${yyyy}-${mm}-${dd}`;

    const hDebut = String(data.heureDebut).padStart(2, '0');
    const hFin = String(data.heureFin).padStart(2, '0');

    const reservationDto = {
      salleId: data.salleSelectionnee.id,
      dateDebut: `${dateStr}T${hDebut}:00:00`,
      dateFin: `${dateStr}T${hFin}:00:00`,
      motif: data.motif,
      nombreParticipants: data.salleSelectionnee.capacite
    };

    console.log('🚀 Envoi de la réservation via Chatbot:', reservationDto);

    this.reservationService.creerReservation(reservationDto).subscribe({
      next: (result) => {
        this.envoyerMessageBot(
          `🎉 **Félicitations !** Votre réservation a été enregistrée avec succès.\n\n` +
          `L'administration l'examinera prochainement. Vous allez être redirigé vers vos réservations.`
        );
        setTimeout(() => {
          this.router.navigate(['/enseignant/mes-reservations']);
        }, 2000);
      },
      error: (error) => {
        console.error('❌ Erreur Chatbot Reservation:', error);
        this.envoyerMessageBot(
          "❌ Désolé, une erreur est survenue lors de l'enregistrement.\n" +
          "Veuillez réessayer ou utiliser le formulaire manuel.",
          [{ label: '📝 Formulaire manuel', type: 'link', data: '/enseignant/reservation-salle' }]
        );
      }
    });
  }

  private afficherAide(): void {
    this.envoyerMessageBot(
      "📚 **Guide d'utilisation de CampusHub**\n\n" +
      "Voici ce que je peux faire pour vous :\n\n" +
      "✅ **Réserver une salle** - Je vous guide étape par étape\n" +
      "✅ **Voir vos réservations** - Accès direct à votre liste\n" +
      "✅ **Consulter le calendrier** - Vue d'ensemble de vos plannings\n" +
      "✅ **Chercher des salles** - Par bâtiment, disponibilité, etc.\n\n" +
      "💬 **Exemples de questions :**\n" +
      "• \"Réserver une salle\"\n" +
      "• \"Quelles salles sont disponibles ?\"\n" +
      "• \"Voir mes réservations\"\n" +
      "• \"Info sur l'amphithéâtre\"\n\n" +
      "N'hésitez pas à me demander ! 😊",
      [
        { label: '📅 Réserver maintenant', type: 'function', data: 'reserver' },
        { label: '🏠 Retour accueil', type: 'function', data: 'accueil' }
      ]
    );
  }

  private afficherMesReservations(): void {
    this.envoyerMessageBot(
      "📋 Vous souhaitez voir vos réservations ?\n\n" +
      "Je vous redirige vers la page complète avec tous les filtres !",
      [
        { label: '📋 Voir mes réservations', type: 'link', data: '/enseignant/mes-reservations' }
      ]
    );
  }

  private afficherCalendrier(): void {
    this.envoyerMessageBot(
      "📅 Le calendrier intelligent vous permet de :\n\n" +
      "✅ Visualiser toutes vos réservations\n" +
      "✅ Détecter les conflits\n" +
      "✅ Obtenir des suggestions de créneaux\n" +
      "✅ Exporter vers Google Calendar\n\n" +
      "Cliquez ci-dessous pour y accéder :",
      [
        { label: '📅 Ouvrir le calendrier', type: 'link', data: '/enseignant/calendrier' }
      ]
    );
  }

  private afficherSallesDisponibles(): void {
    const maintenant = new Date().getHours();
    const sallesLibres = this.salles.filter(s => s.disponible);

    this.envoyerMessageBot(
      `🟢 **Salles disponibles maintenant** (${maintenant}h)\n\n` +
      `✅ ${sallesLibres.length} salles libres sur ${this.salles.length}\n\n` +
      "💡 Pour réserver, utilisez le bouton ci-dessous :",
      [
        { label: '📅 Réserver une salle', type: 'function', data: 'reserver' },
        { label: '🔗 Voir toutes les salles', type: 'link', data: '/enseignant/reservation-salle' }
      ]
    );
  }

  private afficherSallesAnnexe(): void {
    const sallesAnnexe = this.salles.filter(s => s.batiment === 'Bâtiment Annexe');

    this.envoyerMessageBot(
      `💻 **Bâtiment Annexe (Informatique)**\n\n` +
      `📊 ${sallesAnnexe.length} salles disponibles :\n\n` +
      "🏢 **RDC :**\n" +
      "• Salle MAC (30 places)\n" +
      "• LABO Informatique (25 places)\n" +
      "• Salle Polyvalente (150 places)\n\n" +
      "📚 **1er Étage :** Salles 20-27\n" +
      "📚 **2ème Étage :** Salles 30-37\n\n" +
      "Voulez-vous réserver dans ce bâtiment ?",
      [
        { label: '✅ Réserver (Annexe)', type: 'function', data: 'reserver' }
      ]
    );
  }

  private afficherSallesPrincipal(): void {
    const sallesPrincipal = this.salles.filter(s => s.batiment === 'Bâtiment Principal');

    this.envoyerMessageBot(
      `🏭 **Bâtiment Principal (Mécatronique & Industriel)**\n\n` +
      `📊 ${sallesPrincipal.length} salles disponibles :\n\n` +
      "🏢 **RDC :**\n" +
      "• Amphithéâtre Principal (250 places)\n\n" +
      "📚 **1er Étage :** Salles 20-27\n" +
      "📚 **2ème Étage :** Salles 30-37\n" +
      "📚 **3ème Étage :** Salles 40-47\n\n" +
      "Voulez-vous réserver dans ce bâtiment ?",
      [
        { label: '✅ Réserver (Principal)', type: 'function', data: 'reserver' }
      ]
    );
  }

  private afficherInfoAmphi(): void {
    const amphi = this.salles.find(s => s.nom === 'Amphithéâtre Principal');

    if (amphi) {
      this.envoyerMessageBot(
        `🎭 **Amphithéâtre Principal**\n\n` +
        `📍 Bâtiment Principal - RDC\n` +
        `👥 Capacité : ${amphi.capacite} personnes\n\n` +
        `🎯 **Équipements :**\n` +
        amphi.equipements.map(e => `• ${e}`).join('\n') +
        `\n\n💡 Idéal pour : Conférences, examens, grands cours`,
        [
          { label: '📅 Réserver l\'amphi', type: 'function', data: 'reserver' }
        ]
      );
    }
  }

  private afficherInfoLabo(): void {
    const labo = this.salles.find(s => s.nom === 'LABO Informatique');

    if (labo) {
      this.envoyerMessageBot(
        `💻 **LABO Informatique**\n\n` +
        `📍 Bâtiment Annexe - RDC\n` +
        `👥 Capacité : ${labo.capacite} personnes\n\n` +
        `🎯 **Équipements :**\n` +
        labo.equipements.map(e => `• ${e}`).join('\n') +
        `\n\n💡 Idéal pour : TPs, pratique, développement`,
        [
          { label: '📅 Réserver le LABO', type: 'function', data: 'reserver' }
        ]
      );
    }
  }

  private repondreGentiment(): void {
    const reponses = [
      "😊 Avec plaisir ! N'hésitez pas si vous avez d'autres questions.",
      "🎯 Ravi de vous aider ! Bonne journée !",
      "✨ Content d'avoir pu vous aider ! À bientôt !"
    ];

    this.envoyerMessageBot(
      reponses[Math.floor(Math.random() * reponses.length)]
    );
  }

  private saluer(): void {
    const heures = new Date().getHours();
    let salutation = "Bonjour";

    if (heures < 12) salutation = "Bonjour";
    else if (heures < 18) salutation = "Bon après-midi";
    else salutation = "Bonsoir";

    this.envoyerMessageBot(
      `${salutation} ! 👋\n\n` +
      "Je suis **Claude**, votre assistant ENICarthage.\n\n" +
      "Comment puis-je vous aider aujourd'hui ?",
      [
        { label: '📅 Réserver une salle', type: 'function', data: 'reserver' },
        { label: '❓ Voir l\'aide', type: 'function', data: 'aide' }
      ]
    );
  }

  private repondrePasCompris(): void {
    this.envoyerMessageBot(
      "🤔 Je n'ai pas bien compris votre demande.\n\n" +
      "💡 **Suggestions :**\n" +
      "• Essayez \"Réserver une salle\"\n" +
      "• Ou \"Aide\" pour voir ce que je peux faire\n" +
      "• Ou utilisez les boutons ci-dessous :",
      [
        { label: '📅 Réserver', type: 'function', data: 'reserver' },
        { label: '❓ Aide', type: 'function', data: 'aide' },
        { label: '📋 Mes réservations', type: 'link', data: '/enseignant/mes-reservations' }
      ]
    );
  }

  // ========== UTILITAIRES ==========

  private envoyerMessageBot(texte: string, actions?: Action[]): void {
    setTimeout(() => {
      const message: Message = {
        id: this.genererID(),
        texte,
        auteur: 'bot',
        timestamp: new Date(),
        actions
      };

      this.ajouterMessage(message);
    }, 500);
  }

  private ajouterMessage(message: Message): void {
    const messages = this.messagesSubject.value;
    this.messagesSubject.next([...messages, message]);
  }

  private genererID(): string {
    return Math.random().toString(36).substr(2, 9);
  }

  reinitialiser(): void {
    this.messagesSubject.next([]);
    this.conversationState = { etape: 'accueil', data: {} };
    this.envoyerMessageBot(
      "👋 Conversation réinitialisée !\n\n" +
      "Comment puis-je vous aider ?",
      [
        { label: '📅 Réserver une salle', type: 'function', data: 'reserver' },
        { label: '❓ Aide', type: 'function', data: 'aide' }
      ]
    );
  }

  executerAction(action: Action): void {
    if (action.type === 'function') {
      switch (action.data) {
        case 'reserver':
          this.demarrerReservation();
          break;
        case 'aide':
          this.afficherAide();
          break;
        case 'accueil':
          this.saluer();
          break;
        case 'annexe':
        case 'principal':
        case 'tous':
          this.traiterChoixBatiment(action.data);
          break;
        case 'aujourdhui':
        case 'demain':
        case 'lundi':
          this.traiterChoixDate(action.data);
          break;
        case 'finaliser':
          this.finaliserReservation();
          break;
        default:
          if (action.data.includes('-')) {
            this.traiterChoixHeure(action.data);
          }
      }
    }
  }
}

interface ConversationData {
  batiment?: string;
  date?: Date;
  heureDebut?: number;
  heureFin?: number;
  sallesDisponibles?: Salle[];
  salleSelectionnee?: Salle;
  motif?: string;
}

interface ConversationState {
  etape: string;
  data: ConversationData;
}