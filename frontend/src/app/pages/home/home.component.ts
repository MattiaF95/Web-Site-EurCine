import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { map } from 'rxjs';
import { ShowcaseCardComponent } from '../../components/showcase-card/showcase-card.component';
import { Programmazione } from '../../core/model/programmazione.model';
import { ProgrammazioneService } from '../../core/service/programmazione.service';

interface FilmCard {
  title: string;
  subtitle: string;
  date: string;
  times: string;
  lineTwo: string;
}

@Component({
  selector: 'app-home',
  imports: [AsyncPipe, ShowcaseCardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HomeComponent {
  private readonly programmazioneService = inject(ProgrammazioneService);
  readonly cards$ = this.programmazioneService.getAll().pipe(map((items) => this.toFilmCards(items)));

  private toFilmCards(items: Programmazione[]): FilmCard[] {
    const grouped = new Map<string, Programmazione[]>();

    for (const item of items) {
      const key = item.filmTitolo;
      const list = grouped.get(key) ?? [];
      list.push(item);
      grouped.set(key, list);
    }

    return Array.from(grouped.entries())
      .slice(0, 16)
      .map(([filmTitle, list]) => {
        const first = list[0];
        const timeEntries = list
          .map((p) => `${this.formatTime(p.startAt)} (${p.salaNome})`)
          .filter((v, i, arr) => arr.indexOf(v) === i);

        return {
          title: filmTitle,
          subtitle: '',
          date: this.formatDate(first.startAt),
          times: this.formatTimes(timeEntries),
          lineTwo: `Prezzi: ${first.prezzoBasePre18}€ / ${first.prezzoBasePost18}€`
        };
      });
  }

  private formatDate(isoDate: string): string {
    return new Date(isoDate).toLocaleDateString('it-IT');
  }

  private formatTime(isoDate: string): string {
    return new Date(isoDate).toLocaleTimeString('it-IT', { hour: '2-digit', minute: '2-digit' });
  }

  private formatTimes(entries: string[]): string {
    const lines: string[] = [];

    for (let i = 0; i < entries.length; i += 2) {
      lines.push(entries.slice(i, i + 2).join(' | '));
    }

    return lines.join('\n');
  }
}
