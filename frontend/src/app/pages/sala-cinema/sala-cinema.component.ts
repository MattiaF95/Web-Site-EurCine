import { AsyncPipe, DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { map, shareReplay, switchMap, take } from 'rxjs';
import { SeatItem } from '../../core/model/seat-map.model';
import { OrdineService } from '../../core/service/ordine.service';
import { ProgrammazioneService } from '../../core/service/programmazione.service';

@Component({
  selector: 'app-sala-cinema',
  imports: [AsyncPipe, DatePipe, FormsModule],
  templateUrl: './sala-cinema.component.html',
  styleUrl: './sala-cinema.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SalaCinemaComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly programmazioneService = inject(ProgrammazioneService);
  private readonly ordineService = inject(OrdineService);

  readonly selectedSeatIds = signal<Set<number>>(new Set());
  readonly showCustomerInput = signal(false);
  readonly customerName = signal('');

  readonly seatMap$ = this.route.paramMap.pipe(
    map((params) => Number(params.get('programmazioneId'))),
    switchMap((programmazioneId) => this.programmazioneService.getSeatMap(programmazioneId)),
    shareReplay({ bufferSize: 1, refCount: true })
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

  onPrenotaClick(): void {
    if (this.selectedCount() === 0) {
      return;
    }

    this.showCustomerInput.set(true);
  }

  onConfermaOrdine(): void {
    const nomeCliente = this.customerName().trim();
    if (!nomeCliente) {
      return;
    }

    this.seatMap$.pipe(take(1)).subscribe((seatMap) => {
      this.ordineService.createOrdine({
        nomeCliente,
        programmazioneId: seatMap.programmazioneId,
        postoIds: Array.from(this.selectedSeatIds())
      }).subscribe((ordine) => {
        this.selectedSeatIds.set(new Set());
        void this.router.navigate(['/ticket-show', ordine.ordineId]);
      });
    });
  }
}
