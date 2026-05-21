import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { map } from 'rxjs';
import { ScheduleSlot, ShowcaseCardComponent } from '../../components/showcase-card/showcase-card.component';
import { Programmazione } from '../../core/model/programmazione.model';
import { ProgrammazioneService } from '../../core/service/programmazione.service';

interface FilmCard {
  title: string;
  subtitle: string;
  date: string;
  slots: ScheduleSlot[];
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
  private readonly router = inject(Router);

  readonly cards$ = this.programmazioneService.getAll().pipe(map((items) => this.toFilmCards(items)));

  goToSeatMap(programmazioneId: number): void {
    void this.router.navigate(['/programmazione', programmazioneId, 'sala']);
  }

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
        const slots = list
          .map((p) => ({
            programmazioneId: p.programmazioneId,
            label: `${this.formatTime(p.startAt)} (${p.salaNome})`
          }))
          .filter((v, i, arr) => arr.findIndex((x) => x.label === v.label) === i);

        return {
          title: filmTitle,
          subtitle: '',
          date: this.formatDate(first.startAt),
          slots,
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
