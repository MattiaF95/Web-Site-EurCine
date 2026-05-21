import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { map } from 'rxjs';
import { ShowcaseCardComponent } from '../../components/showcase-card/showcase-card.component';
import { SaleService } from '../../core/service/sale.service';

interface SalaCard {
  title: string;
  subtitle: string;
}

@Component({
  selector: 'app-sale',
  imports: [AsyncPipe, ShowcaseCardComponent],
  templateUrl: './sale.component.html',
  styleUrl: './sale.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SaleComponent {
  private readonly saleService = inject(SaleService);
  readonly cards$ = this.saleService.getAll().pipe(
    map((items) =>
      items.map((item) => ({
        title: item.nome,
        subtitle: 'Sala disponibile'
      }))
    )
  );
}
