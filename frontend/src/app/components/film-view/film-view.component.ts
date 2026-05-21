import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { Film } from '../../core/model/film.model';

@Component({
  selector: 'app-film-view',
  templateUrl: './film-view.component.html',
  styleUrl: './film-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilmViewComponent {
  readonly film = input.required<Film>();
}
