import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ChatbotWidget } from "./shared/components/chatbot-widget/chatbot-widget";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ChatbotWidget],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class AppComponent {
  title = 'campushub-frontend';
}