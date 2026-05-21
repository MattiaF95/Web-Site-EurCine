import { ChangeDetectionStrategy, Component, input, output } from '@angular/core';

export interface ScheduleSlot {
  programmazioneId: number;
  label: string;
}

@Component({
  selector: 'app-showcase-card',
  templateUrl: './showcase-card.component.html',
  styleUrl: './showcase-card.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ShowcaseCardComponent {
  readonly title = input.required<string>();
  readonly subtitle = input<string>('');
  readonly date = input<string>('');
  readonly times = input<string>('');
  readonly lineTwo = input<string>('');
  readonly slots = input<ScheduleSlot[]>([]);
  readonly centered = input<boolean>(false);
  readonly large = input<boolean>(false);

  readonly slotSelected = output<number>();

  onSlotClick(programmazioneId: number): void {
    this.slotSelected.emit(programmazioneId);
  }
}
