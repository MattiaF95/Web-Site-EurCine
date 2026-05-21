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
  selector: 'app-programmazione',
  imports: [AsyncPipe, ShowcaseCardComponent],
  templateUrl: './programmazione.component.html',
  styleUrl: './programmazione.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProgrammazioneComponent {
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
        const sale = Array.from(new Set(list.map((p) => p.salaNome))).join(' - ');
        const times = list
          .map((p) => this.formatTime(p.startAt))
          .filter((v, i, arr) => arr.indexOf(v) === i)
          .join(' | ');

        return {
          title: filmTitle,
          subtitle: sale,
          date: this.formatDate(first.startAt),
          times,
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
}
