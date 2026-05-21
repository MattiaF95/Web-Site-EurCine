import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { FilmManagementService } from '../../core/service/film-management.service';
import { AdminFilmTitleOption } from '../../core/model/admin-film/admin-film-management.model';

@Component({
  selector: 'app-admin-film-delete',
  imports: [FormsModule],
  templateUrl: './admin-film-delete.component.html',
  styleUrl: './admin-film-delete.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminFilmDeleteComponent {
  private readonly filmManagementService = inject(FilmManagementService);
  private readonly router = inject(Router);

  readonly titles = signal<AdminFilmTitleOption[]>([]);
  readonly selectedFilmId = signal<number | null>(null);
  readonly loading = signal(true);
  readonly deleting = signal(false);
  readonly error = signal('');
  readonly success = signal('');

  constructor() {
    this.filmManagementService.getFilmTitles().subscribe({
      next: (titles) => {
        this.titles.set(titles);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossibile caricare la lista titoli.');
        this.loading.set(false);
      }
    });
  }

  annulla(): void {
    void this.router.navigate(['/admin/film']);
  }

  cancella(): void {
    const filmId = this.selectedFilmId();
    if (!filmId) {
      this.error.set('Seleziona un film da eliminare.');
      return;
    }

    if (!window.confirm('Confermi la cancellazione del film selezionato?')) {
      return;
    }

    this.error.set('');
    this.success.set('');
    this.deleting.set(true);

    this.filmManagementService.deleteFilm(filmId).subscribe({
      next: () => {
        this.deleting.set(false);
        this.success.set('Film eliminato con successo.');
        this.titles.set(this.titles().filter((item) => item.id !== filmId));
        this.selectedFilmId.set(null);
      },
      error: (err: HttpErrorResponse) => {
        this.deleting.set(false);
        this.error.set((err.error?.message as string) || 'Eliminazione non riuscita.');
      }
    });
  }
}
