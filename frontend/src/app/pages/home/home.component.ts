import { AsyncPipe, CurrencyPipe, DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ProgrammazioneService } from '../../core/service/programmazione.service';

@Component({
  selector: 'app-home',
  imports: [AsyncPipe, DatePipe, CurrencyPipe],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HomeComponent {
  private readonly programmazioneService = inject(ProgrammazioneService);
  readonly programmazioni$ = this.programmazioneService.getAll();
}
