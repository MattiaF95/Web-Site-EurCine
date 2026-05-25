import { AsyncPipe, DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { map, shareReplay, switchMap, take } from 'rxjs';
import { SeatItem } from '../../core/model/seat-map.model';
import { AuthStateService } from '../../core/service/auth-state.service';
import { OrdineService } from '../../core/service/ordine.service';
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
  private readonly router = inject(Router);
  private readonly programmazioneService = inject(ProgrammazioneService);
  private readonly ordineService = inject(OrdineService);
  private readonly authState = inject(AuthStateService);

  readonly selectedSeatIds = signal<Set<number>>(new Set());
  readonly showOrderSummary = signal(false);
  readonly authRequiredMessage = signal('');
  readonly isLoggedIn = this.authState.isLoggedIn;
  readonly session = this.authState.session;

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
    if (!this.isLoggedIn()) {
      this.authRequiredMessage.set('Per completare la prenotazione devi effettuare il login.');
      return;
    }
    this.authRequiredMessage.set('');
    this.showOrderSummary.set(true);
  }

  onConfermaOrdine(): void {
    this.seatMap$.pipe(take(1)).subscribe((seatMap) => {
      this.ordineService.createOrdine({
        programmazioneId: seatMap.programmazioneId,
        postoIds: Array.from(this.selectedSeatIds())
      }).subscribe((ordine) => {
        this.selectedSeatIds.set(new Set());
        this.showOrderSummary.set(false);
        void this.router.navigate(['/ticket-show', ordine.ordineId]);
      });
    });
  }
}
