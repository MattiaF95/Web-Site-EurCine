import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-film',
  templateUrl: './film.component.html',
  styleUrl: './film.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilmComponent {}
