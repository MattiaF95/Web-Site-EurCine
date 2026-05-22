import { ChangeDetectionStrategy, Component, computed, effect, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
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

interface DayOption {
  value: string;
  label: string;
}

@Component({
  selector: 'app-programmazione',
  imports: [ShowcaseCardComponent, FormsModule],
  templateUrl: './programmazione.component.html',
  styleUrl: './programmazione.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProgrammazioneComponent {
  private readonly programmazioneService = inject(ProgrammazioneService);
  private readonly router = inject(Router);

  readonly selectedDate = signal('');

  private readonly allItems = toSignal(this.programmazioneService.getAll(), { initialValue: [] as Programmazione[] });
  private readonly availableDateKeys = toSignal(this.programmazioneService.getAvailableDates(), { initialValue: [] as string[] });

  readonly dayOptions = computed<DayOption[]>(() =>
    this.availableDateKeys().map((value) => ({
      value,
      label: new Date(`${value}T12:00:00`).toLocaleDateString('it-IT', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      })
    }))
  );

  readonly cards = computed(() => {
    const day = this.selectedDate();
    const dayItems = this.allItems().filter((item) => this.toDateKey(item.startAt) === day);
    return this.toFilmCards(dayItems);
  });

  readonly currentDateLabel = computed(() => this.formatLongDate(this.selectedDate()));

  constructor() {
    effect(() => {
      const options = this.dayOptions();
      if (!options.length) {
        this.selectedDate.set('');
        return;
      }

      const current = this.selectedDate();
      if (!current || !options.some((o) => o.value === current)) {
        this.selectedDate.set(options[0].value);
      }
    });
  }

  onDateChange(dateValue: string): void {
    this.selectedDate.set(dateValue);
  }

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
    if (!dateKey) {
      return 'Nessuna data disponibile';
    }

    return new Date(`${dateKey}T12:00:00`).toLocaleDateString('it-IT', {
      weekday: 'long',
      day: 'numeric',
      month: 'long',
      year: 'numeric'
    });
  }
}
