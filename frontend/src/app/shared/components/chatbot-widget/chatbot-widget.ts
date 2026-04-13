import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Chatbot, Message, Action } from '../../../features/enseignant/services/chatbot';
import { MarkdownToHtmlPipe } from '../../pipes/markdown-to-html-pipe';

@Component({
  selector: 'app-chatbot-widget',
  standalone: true,
  imports: [CommonModule, FormsModule, MarkdownToHtmlPipe],
  templateUrl: './chatbot-widget.html',
  styleUrls: ['./chatbot-widget.scss']
})
export class ChatbotWidget implements OnInit, OnDestroy {
  @ViewChild('messagesContainer') messagesContainer!: ElementRef;

  isOpen = false;
  isMinimized = false;
  messages: Message[] = [];
  messageInput = '';
  isTyping = false;

  private messagesSubscription?: Subscription;

  constructor(
    public chatbotService: Chatbot,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.messagesSubscription = this.chatbotService.messages$.subscribe({
      next: (messages) => {
        this.messages = messages;
        setTimeout(() => this.scrollToBottom(), 100);
      }
    });
  }

  ngOnDestroy(): void {
    this.messagesSubscription?.unsubscribe();
  }

  toggleChat(): void {
    this.isOpen = !this.isOpen;
    if (this.isOpen) {
      this.isMinimized = false;
      setTimeout(() => this.scrollToBottom(), 100);
    }
  }

  minimizeChat(): void {
    this.isMinimized = !this.isMinimized;
  }

  closeChat(): void {
    this.isOpen = false;
  }

  envoyerMessage(): void {
    if (!this.messageInput.trim()) return;

    this.chatbotService.envoyerMessageUtilisateur(this.messageInput);
    this.messageInput = '';
    this.simulateTyping();
  }

  executerAction(action: Action): void {
    if (action.type === 'link') {
      this.router.navigate([action.data]);
      this.closeChat();
    } else if (action.type === 'function') {
      this.chatbotService.executerAction(action);
      this.simulateTyping();
    }
  }

  reinitialiser(): void {
    if (confirm('Voulez-vous vraiment réinitialiser la conversation ?')) {
      this.chatbotService.reinitialiser();
    }
  }

  private simulateTyping(): void {
    this.isTyping = true;
    setTimeout(() => {
      this.isTyping = false;
    }, 1000);
  }

  private scrollToBottom(): void {
    try {
      if (this.messagesContainer) {
        this.messagesContainer.nativeElement.scrollTop =
          this.messagesContainer.nativeElement.scrollHeight;
      }
    } catch (err) {
      console.error('Erreur scroll:', err);
    }
  }

  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.envoyerMessage();
    }
  }

  getMessageTime(timestamp: Date): string {
    return new Date(timestamp).toLocaleTimeString('fr-FR', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}