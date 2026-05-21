import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { FilmManagementService } from '../../core/service/film-management.service';
import { AdminCatalogOption } from '../../core/model/admin-film/admin-film-management.model';

@Component({
  selector: 'app-admin-film-create',
  imports: [FormsModule],
  templateUrl: './admin-film-create.component.html',
  styleUrl: './admin-film-create.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminFilmCreateComponent {
  private readonly filmManagementService = inject(FilmManagementService);
  private readonly router = inject(Router);

  readonly titolo = signal('');
  readonly durataMin = signal<number | null>(null);
  readonly linguaId = signal<number | null>(null);
  readonly selectedGenereIds = signal<number[]>([]);
  readonly genreMenuOpen = signal(false);

  readonly lingue = signal<AdminCatalogOption[]>([]);
  readonly generi = signal<AdminCatalogOption[]>([]);
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly error = signal('');
  readonly success = signal('');

  constructor() {
    this.filmManagementService.getMeta().subscribe({
      next: (meta) => {
        this.lingue.set(meta.lingue);
        this.generi.set(meta.generi);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossibile caricare lingue e generi.');
        this.loading.set(false);
      }
    });
  }

  toggleGenreMenu(): void {
    this.genreMenuOpen.set(!this.genreMenuOpen());
  }

  closeGenreMenu(): void {
    this.genreMenuOpen.set(false);
  }

  onGenreItemClick(event: MouseEvent, genereId: number): void {
    event.stopPropagation();
    const selected = this.selectedGenereIds();
    if (selected.includes(genereId)) {
      this.selectedGenereIds.set(selected.filter((id) => id !== genereId));
      return;
    }
    this.selectedGenereIds.set([...selected, genereId]);
  }

  isGenereSelected(genereId: number): boolean {
    return this.selectedGenereIds().includes(genereId);
  }

  selectedGenereLabel(): string {
    const ids = this.selectedGenereIds();
    if (!ids.length) {
      return 'Seleziona generi';
    }

    const names = this.generi()
      .filter((g) => ids.includes(g.id))
      .map((g) => g.nome);

    return names.join(', ');
  }

  salva(): void {
    if (!this.titolo().trim() || !this.durataMin() || !this.linguaId()) {
      this.error.set('Compila titolo, durata e lingua.');
      return;
    }

    this.error.set('');
    this.success.set('');
    this.saving.set(true);

    this.filmManagementService
      .createFilm({
        titolo: this.titolo().trim(),
        durataMin: this.durataMin() as number,
        linguaId: this.linguaId() as number,
        genereIds: this.selectedGenereIds()
      })
      .subscribe({
        next: () => {
          this.saving.set(false);
          this.success.set('Film creato con successo.');
          this.titolo.set('');
          this.durataMin.set(null);
          this.linguaId.set(null);
          this.selectedGenereIds.set([]);
          this.closeGenreMenu();
        },
        error: (err: HttpErrorResponse) => {
          this.saving.set(false);
          this.error.set((err.error?.message as string) || 'Creazione film non riuscita.');
        }
      });
  }

  annulla(): void {
    void this.router.navigate(['/admin/film']);
  }
}
