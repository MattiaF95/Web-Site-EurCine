import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { Sala } from '../../core/model/sala.model';

@Component({
  selector: 'app-sala-view',
  templateUrl: './sala-view.component.html',
  styleUrl: './sala-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SalaViewComponent {
  readonly sala = input.required<Sala>();

  caratteristicheList(): string[] {
    return (this.sala().caratteristicheNomi ?? '')
      .split(',')
      .map((item) => item.trim())
      .filter(Boolean);
  }
}
