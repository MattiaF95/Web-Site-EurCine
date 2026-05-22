import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import {
  AdminCatalogOption,
  AdminFilmTitleOption,
  AdminProgrammazioneCreatedItem
} from '../../core/model/admin-film/admin-film-management.model';
import { AdminProgrammazioneManagementService } from '../../core/service/admin-programmazione-management.service';

interface ProgrammazioneRow {
  salaId: number | null;
  orario: string;
}

interface DeleteDayGroup {
  dayKey: string;
  dayLabel: string;
  items: AdminProgrammazioneCreatedItem[];
}

@Component({
  selector: 'app-admin-programmazione-management',
  imports: [FormsModule],
  templateUrl: './admin-programmazione-management.component.html',
  styleUrl: './admin-programmazione-management.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminProgrammazioneManagementComponent {
  private readonly adminProgrammazioneService = inject(AdminProgrammazioneManagementService);

  readonly mode = signal<'add' | 'delete'>('add');

  readonly giorno = signal('');
  readonly filmId = signal<number | null>(null);
  readonly rows = signal<ProgrammazioneRow[]>([{ salaId: null, orario: '' }]);

  readonly deleteFilmId = signal<number | null>(null);
  readonly deleteItems = signal<AdminProgrammazioneCreatedItem[]>([]);
  readonly deletingProgrammazioneId = signal<number | null>(null);
  readonly loadingDeleteItems = signal(false);

  readonly filmOptions = signal<AdminFilmTitleOption[]>([]);
  readonly saleOptions = signal<AdminCatalogOption[]>([]);
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly createdSummary = signal<string[]>([]);

  readonly showFilmSelect = computed(() => !!this.giorno());
  readonly showRows = computed(() => this.showFilmSelect() && !!this.filmId());
  readonly canSubmit = computed(() => {
    if (!this.giorno() || !this.filmId() || !this.rows().length) {
      return false;
    }

    const first = this.rows()[0];
    return !!first.salaId && !!first.orario;
  });

  readonly deleteGroups = computed<DeleteDayGroup[]>(() => {
    const grouped = new Map<string, AdminProgrammazioneCreatedItem[]>();
    for (const item of this.deleteItems()) {
      const dayKey = this.toDateKey(item.startAt);
      const list = grouped.get(dayKey) ?? [];
      list.push(item);
      grouped.set(dayKey, list);
    }

    return Array.from(grouped.entries()).map(([dayKey, items]) => ({
      dayKey,
      dayLabel: new Date(`${dayKey}T12:00:00`).toLocaleDateString('it-IT', {
        weekday: 'long',
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      }),
      items
    }));
  });

  constructor() {
    this.adminProgrammazioneService.getCatalog().subscribe({
      next: (catalog) => {
        this.filmOptions.set(catalog.film);
        this.saleOptions.set(catalog.sale);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossibile caricare catalogo film/sale.');
        this.loading.set(false);
      }
    });
  }

  setMode(nextMode: 'add' | 'delete'): void {
    this.mode.set(nextMode);
    this.error.set('');
    this.success.set('');
    this.createdSummary.set([]);
  }

  onGiornoChange(value: string): void {
    this.giorno.set(value);
    this.filmId.set(null);
    this.rows.set([{ salaId: null, orario: '' }]);
    this.success.set('');
    this.error.set('');
    this.createdSummary.set([]);
  }

  onFilmChange(value: number | null): void {
    this.filmId.set(value);
    this.rows.set([{ salaId: null, orario: '' }]);
    this.success.set('');
    this.error.set('');
    this.createdSummary.set([]);
  }

  onDeleteFilmChange(value: number | null): void {
    this.deleteFilmId.set(value);
    this.deleteItems.set([]);
    this.error.set('');
    this.success.set('');
    this.createdSummary.set([]);

    if (!value) {
      return;
    }

    this.loadingDeleteItems.set(true);
    this.adminProgrammazioneService.getByFilm(value).subscribe({
      next: (items) => {
        this.loadingDeleteItems.set(false);
        this.deleteItems.set(items);
      },
      error: (err: HttpErrorResponse) => {
        this.loadingDeleteItems.set(false);
        this.error.set((err.error?.message as string) || 'Impossibile caricare gli orari del film.');
      }
    });
  }

  updateRowSala(index: number, salaId: number | null): void {
    const next = [...this.rows()];
    next[index] = { ...next[index], salaId };
    this.rows.set(next);
  }

  updateRowOrario(index: number, orario: string): void {
    const next = [...this.rows()];
    next[index] = { ...next[index], orario };
    this.rows.set(next);
  }

  addRow(index: number): void {
    const row = this.rows()[index];
    if (!row?.salaId || !row.orario) {
      this.error.set('Completa prima sala e orario della riga corrente.');
      return;
    }

    const next = [...this.rows()];
    next.splice(index + 1, 0, { salaId: null, orario: '' });
    this.rows.set(next);
    this.error.set('');
  }

  submit(): void {
    if (!this.canSubmit()) {
      return;
    }

    const hasIncompleteRows = this.rows().some((r) => !r.salaId || !r.orario);
    if (hasIncompleteRows) {
      this.error.set('Completa o rimuovi le righe incomplete prima di inviare.');
      return;
    }

    const seen = new Set<string>();
    for (const row of this.rows()) {
      const key = `${row.salaId}|${row.orario}`;
      if (seen.has(key)) {
        this.error.set('Ci sono righe duplicate con stessa sala e orario.');
        return;
      }
      seen.add(key);
    }

    const validItems = this.rows().filter((r) => !!r.salaId && !!r.orario);
    this.error.set('');
    this.success.set('');
    this.createdSummary.set([]);
    this.saving.set(true);

    this.adminProgrammazioneService.createBatch({
      giorno: this.giorno(),
      filmId: this.filmId() as number,
      items: validItems.map((r) => ({ salaId: r.salaId as number, orario: r.orario }))
    }).subscribe({
      next: (res) => {
        this.saving.set(false);
        this.success.set(res.message);
        this.createdSummary.set(
          res.created.map((c) => `${c.filmTitolo} - ${c.salaNome} - ${new Date(c.startAt).toLocaleString('it-IT')}`)
        );
      },
      error: (err: HttpErrorResponse) => {
        this.saving.set(false);
        const backendMessage = typeof err.error?.message === 'string' ? err.error.message : '';
        if (backendMessage) {
          this.error.set(backendMessage);
          return;
        }
        if (err.status === 409) {
          this.error.set('Conflitto di programmazione: sala occupata nello stesso intervallo orario.');
          return;
        }
        this.error.set('Inserimento programmazione non riuscito.');
      }
    });
  }

  deleteProgrammazione(item: AdminProgrammazioneCreatedItem): void {
    const when = new Date(item.startAt).toLocaleString('it-IT');
    const shouldDelete = window.confirm(`Vuoi eliminare ${item.filmTitolo} in ${item.salaNome} alle ${when}?`);
    if (!shouldDelete) {
      return;
    }

    this.error.set('');
    this.success.set('');
    this.deletingProgrammazioneId.set(item.programmazioneId);

    this.adminProgrammazioneService.deleteProgrammazione(item.programmazioneId).subscribe({
      next: () => {
        this.deletingProgrammazioneId.set(null);
        this.deleteItems.set(this.deleteItems().filter((p) => p.programmazioneId !== item.programmazioneId));
        this.success.set('Programmazione eliminata con successo.');
      },
      error: (err: HttpErrorResponse) => {
        this.deletingProgrammazioneId.set(null);
        this.error.set((err.error?.message as string) || 'Eliminazione programmazione non riuscita.');
      }
    });
  }

  formatTime(isoDate: string): string {
    return new Date(isoDate).toLocaleTimeString('it-IT', { hour: '2-digit', minute: '2-digit' });
  }

  private toDateKey(isoDate: string): string {
    const [datePart] = isoDate.split('T');
    return datePart ?? isoDate;
  }
}
