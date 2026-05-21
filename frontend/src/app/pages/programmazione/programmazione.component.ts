import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
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

  private readonly weekStart = '2026-06-01';
  readonly selectedDate = signal(this.weekStart);
  readonly dayOptions: DayOption[] = this.buildDayOptions(this.weekStart, 7);

  private readonly allItems = toSignal(this.programmazioneService.getAll(), { initialValue: [] as Programmazione[] });

  readonly cards = computed(() => {
    const day = this.selectedDate();
    const dayItems = this.allItems().filter((item) => this.toDateKey(item.startAt) === day);
    return this.toFilmCards(dayItems);
  });

  readonly currentDateLabel = computed(() => this.formatLongDate(this.selectedDate()));

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

  private buildDayOptions(startDate: string, days: number): DayOption[] {
    const options: DayOption[] = [];
    const base = new Date(`${startDate}T12:00:00`);

    for (let i = 0; i < days; i++) {
      const d = new Date(base);
      d.setDate(base.getDate() + i);
      const value = d.toISOString().slice(0, 10);
      options.push({
        value,
        label: d.toLocaleDateString('it-IT', { day: '2-digit', month: '2-digit', year: 'numeric' })
      });
    }

    return options;
  }

  private toDateKey(isoDate: string): string {
    return new Date(isoDate).toISOString().slice(0, 10);
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
