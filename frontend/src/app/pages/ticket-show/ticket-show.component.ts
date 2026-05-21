import { AsyncPipe, CurrencyPipe, DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { map, switchMap } from 'rxjs';
import { OrdineService } from '../../core/service/ordine.service';

@Component({
  selector: 'app-ticket-show',
  imports: [AsyncPipe, DatePipe, CurrencyPipe],
  templateUrl: './ticket-show.component.html',
  styleUrl: './ticket-show.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TicketShowComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly ordineService = inject(OrdineService);

  readonly ordine$ = this.route.paramMap.pipe(
    map((params) => Number(params.get('ordineId'))),
    switchMap((ordineId) => this.ordineService.getOrdine(ordineId))
  );

  scaricaBiglietti(ordineId: number): void {
    this.ordineService.getBiglietti(ordineId).subscribe((tickets) => {
      const content = tickets
        .map((t) => `${t.bigliettoId};${t.filmTitolo};${t.salaNome};${t.startAt};${t.fila}${t.postoNumero};${t.prezzo}`)
        .join('\n');

      const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `biglietti-ordine-${ordineId}.txt`;
      a.click();
      URL.revokeObjectURL(url);
    });
  }
}
