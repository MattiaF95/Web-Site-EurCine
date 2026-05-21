import { ChangeDetectionStrategy, Component, input } from '@angular/core';

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
  readonly centered = input<boolean>(false);
  readonly large = input<boolean>(false);
}
