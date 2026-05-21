import { AsyncPipe, DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { map, switchMap } from 'rxjs';
import { SeatItem } from '../../core/model/seat-map.model';
import { ProgrammazioneService } from '../../core/service/programmazione.service';

@Component({
  selector: 'app-sala-cinema',
  imports: [AsyncPipe, DatePipe],
  templateUrl: './sala-cinema.component.html',
  styleUrl: './sala-cinema.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SalaCinemaComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly programmazioneService = inject(ProgrammazioneService);

  readonly selectedSeatIds = signal<Set<number>>(new Set());

  readonly seatMap$ = this.route.paramMap.pipe(
    map((params) => Number(params.get('programmazioneId'))),
    switchMap((programmazioneId) => this.programmazioneService.getSeatMap(programmazioneId))
  );

  readonly selectedCount = computed(() => this.selectedSeatIds().size);

  toggleSeat(seat: SeatItem): void {
    if (seat.stato !== 'AVAILABLE') {
      return;
    }

    const current = new Set(this.selectedSeatIds());
    if (current.has(seat.postoId)) {
      current.delete(seat.postoId);
    } else {
      current.add(seat.postoId);
    }

    this.selectedSeatIds.set(current);
  }

  isSelected(seatId: number): boolean {
    return this.selectedSeatIds().has(seatId);
  }

  getSelectedSeatLabels(righe: Array<{ fila: string; posti: Array<{ postoId: number; numero: number }> }>): string[] {
    const selected = this.selectedSeatIds();
    const labels: string[] = [];

    for (const row of righe) {
      for (const seat of row.posti) {
        if (selected.has(seat.postoId)) {
          labels.push(`${row.fila}${seat.numero}`);
        }
      }
    }

    return labels;
  }
}
