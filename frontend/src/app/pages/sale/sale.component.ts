import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-sale',
  templateUrl: './sale.component.html',
  styleUrl: './sale.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SaleComponent {}
