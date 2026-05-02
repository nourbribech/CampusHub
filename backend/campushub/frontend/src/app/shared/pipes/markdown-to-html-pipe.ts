import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Pipe({
  name: 'markdownToHtml',
  standalone: true
})
export class MarkdownToHtmlPipe implements PipeTransform {

  constructor(private sanitizer: DomSanitizer) { }

  transform(value: string): SafeHtml {
    if (!value) return '';

    let html = value;

    // Gras **texte**
    html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

    // Italique *texte*
    html = html.replace(/\*(.*?)\*/g, '<em>$1</em>');

    // Retours à la ligne
    html = html.replace(/\n/g, '<br>');

    // Listes à puces
    html = html.replace(/^• (.+)$/gm, '<li>$1</li>');
    html = html.replace(/(<li>.*<\/li>)/s, '<ul class="list-disc pl-5 my-2">$1</ul>');

    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
}