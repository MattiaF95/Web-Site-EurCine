import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { SaleService } from '../../core/service/sale.service';

@Component({
  selector: 'app-sale',
  imports: [AsyncPipe],
  templateUrl: './sale.component.html',
  styleUrl: './sale.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SaleComponent {
  private readonly saleService = inject(SaleService);
  readonly sale$ = this.saleService.getAll();
}
