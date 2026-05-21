import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FilmService } from '../../core/service/film.service';

@Component({
  selector: 'app-film',
  imports: [AsyncPipe],
  templateUrl: './film.component.html',
  styleUrl: './film.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilmComponent {
  private readonly filmService = inject(FilmService);
  readonly films$ = this.filmService.getAll();
}
