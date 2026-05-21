import { AsyncPipe, CurrencyPipe, DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ProgrammazioneService } from '../../core/service/programmazione.service';

@Component({
  selector: 'app-programmazione',
  imports: [AsyncPipe, DatePipe, CurrencyPipe],
  templateUrl: './programmazione.component.html',
  styleUrl: './programmazione.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProgrammazioneComponent {
  private readonly programmazioneService = inject(ProgrammazioneService);
  readonly programmazioni$ = this.programmazioneService.getAll();
}
