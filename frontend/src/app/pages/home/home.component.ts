import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { ScheduleSlot, ShowcaseCardComponent } from '../../components/showcase-card/showcase-card.component';
import { Programmazione } from '../../core/model/programmazione.model';
import { ProgrammazioneService } from '../../core/service/programmazione.service';

interface FilmCard {
  title: string;
  subtitle: string;
  slots: ScheduleSlot[];
  lineTwo: string;
}

@Component({
  selector: 'app-home',
  imports: [ShowcaseCardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HomeComponent {
  private readonly programmazioneService = inject(ProgrammazioneService);
  private readonly router = inject(Router);

  private readonly defaultDate = '2026-06-01';
  readonly displayDate = this.formatLongDate(this.defaultDate);

  private readonly allItems = toSignal(this.programmazioneService.getAll(), { initialValue: [] as Programmazione[] });

  readonly cards = computed(() => {
    const dayItems = this.allItems().filter((item) => this.toDateKey(item.startAt) === this.defaultDate);
    return this.toFilmCards(dayItems);
  });

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
          slots,
          lineTwo: `Prezzi: ${first.prezzoBasePre18}€ / ${first.prezzoBasePost18}€`
        };
      });
  }

  private toDateKey(isoDate: string): string {
    const [datePart] = isoDate.split('T');
    return datePart ?? isoDate;
  }

  private formatTime(isoDate: string): string {
    return new Date(isoDate).toLocaleTimeString('it-IT', { hour: '2-digit', minute: '2-digit' });
  }

  private formatLongDate(dateKey: string): string {
    return new Date(`${dateKey}T12:00:00`).toLocaleDateString('it-IT', {
      weekday: 'long',
      day: 'numeric',
      month: 'long',
      year: 'numeric'
    });
  }
}
