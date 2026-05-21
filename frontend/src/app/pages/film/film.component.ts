import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs';
import { FilmViewComponent } from '../../components/film-view/film-view.component';
import { Film } from '../../core/model/film.model';
import { FilmService } from '../../core/service/film.service';

@Component({
  selector: 'app-film',
  imports: [FilmViewComponent],
  templateUrl: './film.component.html',
  styleUrl: './film.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilmComponent {
  private readonly filmService = inject(FilmService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly films = toSignal(this.filmService.getAll(), { initialValue: [] as Film[] });

  private readonly query = toSignal(
    this.route.queryParamMap.pipe(
      map((params) => ({
        title: (params.get('title') ?? '').trim(),
        q: (params.get('q') ?? '').trim()
      }))
    ),
    { initialValue: { title: '', q: '' } }
  );

  readonly selectedFilm = computed(() => {
    const all = this.films();
    const { title, q } = this.query();

    if (title) {
      const exact = all.find((f) => f.titolo.toLowerCase() === title.toLowerCase());
      return exact ?? null;
    }

    if (q) {
      const lower = q.toLowerCase();
      const partial = all.find((f) => f.titolo.toLowerCase().includes(lower));
      return partial ?? null;
    }

    return null;
  });

  readonly hasSelectedFilm = computed(() => !!this.selectedFilm());

  openFilm(film: Film): void {
    void this.router.navigate(['/film'], { queryParams: { title: film.titolo } });
  }

  viewAll(): void {
    void this.router.navigate(['/film']);
  }
}
