import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FilmService } from '../../core/service/film.service';
import { Film } from '../../core/model/film.model';

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

  getGeneri(film: Film): string {
    if (!film.generi || film.generi.length === 0) {
      return '-';
    }

    return film.generi.map((genere) => genere.nome).join(', ');
  }
}
