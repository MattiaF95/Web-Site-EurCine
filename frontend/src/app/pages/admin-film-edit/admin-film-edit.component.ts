import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { FilmManagementService } from '../../core/service/film-management.service';
import {
  AdminCatalogOption,
  AdminFilmFormData,
  AdminFilmTitleOption
} from '../../core/model/admin-film/admin-film-management.model';

@Component({
  selector: 'app-admin-film-edit',
  imports: [FormsModule],
  templateUrl: './admin-film-edit.component.html',
  styleUrl: './admin-film-edit.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminFilmEditComponent {
  private readonly filmManagementService = inject(FilmManagementService);
  private readonly router = inject(Router);

  readonly titles = signal<AdminFilmTitleOption[]>([]);
  readonly lingue = signal<AdminCatalogOption[]>([]);
  readonly generi = signal<AdminCatalogOption[]>([]);

  readonly selectedFilmId = signal<number | null>(null);
  readonly selectedFilm = signal<AdminFilmFormData | null>(null);
  readonly genreMenuOpen = signal(false);
  readonly loading = signal(true);
  readonly loadingFilm = signal(false);
  readonly saving = signal(false);
  readonly error = signal('');
  readonly success = signal('');

  constructor() {
    this.loadInitial();
  }

  private loadInitial(): void {
    this.filmManagementService.getFilmTitles().subscribe({
      next: (titles) => {
        this.titles.set(titles);
        this.filmManagementService.getMeta().subscribe({
          next: (meta) => {
            this.lingue.set(meta.lingue);
            this.generi.set(meta.generi);
            this.loading.set(false);
          },
          error: () => {
            this.error.set('Impossibile caricare i metadati.');
            this.loading.set(false);
          }
        });
      },
      error: () => {
        this.error.set('Impossibile caricare la lista titoli.');
        this.loading.set(false);
      }
    });
  }

  onFilmSelect(filmId: number | null): void {
    this.selectedFilmId.set(filmId);
    this.selectedFilm.set(null);
    this.success.set('');
    this.error.set('');
    this.closeGenreMenu();

    if (!filmId) {
      return;
    }

    this.loadingFilm.set(true);
    this.filmManagementService.getFilmById(filmId).subscribe({
      next: (film) => {
        this.selectedFilm.set(film);
        this.loadingFilm.set(false);
      },
      error: () => {
        this.error.set('Impossibile caricare i dettagli del film.');
        this.loadingFilm.set(false);
      }
    });
  }

  updateField<K extends keyof AdminFilmFormData>(key: K, value: AdminFilmFormData[K]): void {
    const current = this.selectedFilm();
    if (!current) {
      return;
    }
    this.selectedFilm.set({ ...current, [key]: value });
  }

  toggleGenreMenu(): void {
    this.genreMenuOpen.set(!this.genreMenuOpen());
  }

  closeGenreMenu(): void {
    this.genreMenuOpen.set(false);
  }

  onGenreItemClick(event: MouseEvent, genereId: number): void {
    event.stopPropagation();
    const current = this.selectedFilm();
    if (!current) {
      return;
    }

    if (current.genereIds.includes(genereId)) {
      this.updateField(
        'genereIds',
        current.genereIds.filter((id) => id !== genereId)
      );
      return;
    }

    this.updateField('genereIds', [...current.genereIds, genereId]);
  }

  isGenereSelected(genereId: number): boolean {
    const current = this.selectedFilm();
    return current ? current.genereIds.includes(genereId) : false;
  }

  selectedGenereLabel(): string {
    const current = this.selectedFilm();
    if (!current || current.genereIds.length === 0) {
      return 'Seleziona generi';
    }

    const names = this.generi()
      .filter((g) => current.genereIds.includes(g.id))
      .map((g) => g.nome);

    return names.join(', ');
  }

  salva(): void {
    const film = this.selectedFilm();
    if (!film) {
      this.error.set('Seleziona un film da modificare.');
      return;
    }

    this.error.set('');
    this.success.set('');
    this.saving.set(true);

    if (!film.titolo.trim() || !film.durataMin || !film.linguaId || !film.trama.trim()) {
      this.saving.set(false);
      this.error.set('Compila titolo, durata, lingua e trama.');
      return;
    }

    this.filmManagementService
      .updateFilm(film.id, {
        titolo: film.titolo.trim(),
        durataMin: film.durataMin,
        linguaId: film.linguaId,
        trama: film.trama.trim(),
        genereIds: film.genereIds
      })
      .subscribe({
        next: (updated) => {
          this.selectedFilm.set(updated);
          this.saving.set(false);
          this.success.set('Film aggiornato con successo.');
          this.closeGenreMenu();
        },
        error: (err: HttpErrorResponse) => {
          this.saving.set(false);
          this.error.set((err.error?.message as string) || 'Aggiornamento non riuscito.');
        }
      });
  }

  annulla(): void {
    void this.router.navigate(['/admin/film']);
  }
}
