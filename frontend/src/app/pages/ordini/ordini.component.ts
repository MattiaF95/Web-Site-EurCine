import { AsyncPipe, CurrencyPipe, DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { map } from 'rxjs';
import { OrdineService } from '../../core/service/ordine.service';

@Component({
  selector: 'app-ordini',
  imports: [AsyncPipe, DatePipe, CurrencyPipe, RouterLink],
  templateUrl: './ordini.component.html',
  styleUrl: './ordini.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OrdiniComponent {
  private readonly ordineService = inject(OrdineService);

  readonly ordini$ = this.ordineService.getOrdini().pipe(map((items) => items ?? []));
}
