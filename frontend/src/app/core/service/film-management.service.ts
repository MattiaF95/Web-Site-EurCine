import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AdminFilmFormData,
  AdminFilmMetaResponse,
  AdminFilmSaveRequest,
  AdminFilmTitleOption,
  AdminProgrammazioneCatalogResponse
} from '../model/admin-film/admin-film-management.model';
import { SessionSandboxService } from './session-sandbox.service';

type PublicFilm = {
  titolo: string;
  durataMin: number;
  linguaNome: string;
  trama: string;
  generiNomi: string;
};

@Injectable({ providedIn: 'root' })
export class FilmManagementService {
  private readonly http = inject(HttpClient);
  private readonly sandbox = inject(SessionSandboxService);
  private readonly apiUrl = `${environment.apiBaseUrl}/api/admin`;

  getFilmTitles(): Observable<AdminFilmTitleOption[]> {
    return this.http.get<AdminFilmTitleOption[]>(`${this.apiUrl}/film/titoli`);
  }

  getMeta(): Observable<AdminFilmMetaResponse> {
    return this.http.get<AdminFilmMetaResponse>(`${this.apiUrl}/film/meta`);
  }

  getFilmById(filmId: number): Observable<AdminFilmFormData> {
    return this.http.get<AdminFilmFormData>(`${this.apiUrl}/film/${filmId}`);
  }

  createFilm(payload: AdminFilmSaveRequest): Observable<AdminFilmFormData> {
    if (environment.production && this.sandbox.isActive()) {
      return this.sandboxCall(() => this.createMockFilm(payload));
    }

    return this.http.post<AdminFilmFormData>(`${this.apiUrl}/film/aggiungi`, payload);
  }

  updateFilm(filmId: number, payload: AdminFilmSaveRequest): Observable<AdminFilmFormData> {
    if (environment.production && this.sandbox.isActive()) {
      return this.sandboxCall(() => this.updateMockFilm(filmId, payload));
    }

    return this.http.put<AdminFilmFormData>(`${this.apiUrl}/film/modifica/${filmId}`, payload);
  }

  deleteFilm(filmId: number): Observable<void> {
    if (environment.production && this.sandbox.isActive()) {
      return this.sandboxCall(() => {
        this.deleteMockFilm(filmId);
        return void 0;
      });
    }

    return this.http.delete<void>(`${this.apiUrl}/film/elimina/${filmId}`);
  }

  private createMockFilm(payload: AdminFilmSaveRequest): AdminFilmFormData {
    const state = this.sandbox.getFilmState();
    const film: AdminFilmFormData = {
      id: state.nextFilmId,
      titolo: payload.titolo.trim(),
      durataMin: payload.durataMin,
      linguaId: payload.linguaId,
      trama: payload.trama.trim(),
      genereIds: [...payload.genereIds]
    };

    this.sandbox.setFilmState((draft) => {
      draft.nextFilmId += 1;
      draft.deletedIds = draft.deletedIds.filter((id) => id !== film.id);
      delete draft.updated[film.id];
      draft.created.push(film);
    });

    this.syncFilmCaches();
    this.syncProgrammazioneCatalogCaches();
    return film;
  }

  private updateMockFilm(filmId: number, payload: AdminFilmSaveRequest): AdminFilmFormData {
    const film: AdminFilmFormData = {
      id: filmId,
      titolo: payload.titolo.trim(),
      durataMin: payload.durataMin,
      linguaId: payload.linguaId,
      trama: payload.trama.trim(),
      genereIds: [...payload.genereIds]
    };

    this.sandbox.setFilmState((draft) => {
      draft.created = draft.created.filter((item) => item.id !== filmId);
      draft.updated[filmId] = film;
      draft.deletedIds = draft.deletedIds.filter((id) => id !== filmId);
    });

    this.syncFilmCaches();
    this.syncProgrammazioneCatalogCaches();
    return film;
  }

  private deleteMockFilm(filmId: number): void {
    this.sandbox.setFilmState((draft) => {
      draft.created = draft.created.filter((item) => item.id !== filmId);
      delete draft.updated[filmId];
      if (!draft.deletedIds.includes(filmId)) {
        draft.deletedIds.push(filmId);
      }
    });

    this.syncFilmCaches();
    this.syncProgrammazioneCatalogCaches();
  }

  private syncFilmCaches(): void {
    const titles = this.buildTitlesFromState();
    this.sandbox.setCacheResponse('/api/admin/film/titoli', titles);

    const meta = this.buildMetaFromState();
    this.sandbox.setCacheResponse('/api/admin/film/meta', meta);

    const adminDetails = titles.map((item) => this.buildFilmById(item.id));
    for (const film of adminDetails) {
      if (film) {
        this.sandbox.setCacheResponse(`/api/admin/film/${film.id}`, film);
      }
    }

    const publicFilms = this.buildPublicFilmList(meta);
    if (publicFilms.length > 0) {
      this.sandbox.setCacheResponse('/api/film', publicFilms);
    }
  }

  private syncProgrammazioneCatalogCaches(): void {
    const catalog = this.sandbox.readCacheBody<AdminProgrammazioneCatalogResponse>('GET', '/api/admin/programmazione/catalog');
    if (!catalog) {
      return;
    }

    const titles = this.buildTitlesFromState();
    this.sandbox.setCacheResponse('/api/admin/programmazione/catalog', {
      ...catalog,
      film: titles
    });
  }

  private buildTitlesFromState(): AdminFilmTitleOption[] {
    const films = this.getMergedFilms();
    return films.map((item) => ({ id: item.id, titolo: item.titolo }));
  }

  private buildMetaFromState(): AdminFilmMetaResponse {
    const films = this.getMergedFilms();
    const lingue = new Map<number, string>();
    const generi = new Map<number, string>();

    for (const film of films) {
      if (!lingue.has(film.linguaId)) {
        lingue.set(film.linguaId, this.buildLanguageName(film));
      }
      for (const genereId of film.genereIds) {
        if (!generi.has(genereId)) {
          generi.set(genereId, this.buildGenreName(genereId));
        }
      }
    }

    return {
      lingue: Array.from(lingue.entries()).map(([id, nome]) => ({ id, nome })),
      generi: Array.from(generi.entries()).map(([id, nome]) => ({ id, nome }))
    };
  }

  private buildFilmById(filmId: number): AdminFilmFormData | null {
    return this.getMergedFilms().find((item) => item.id === filmId) ?? null;
  }

  private buildPublicFilmList(meta: AdminFilmMetaResponse): PublicFilm[] {
    const films = this.getMergedFilms();
    return films.map((item) => ({
      titolo: item.titolo,
      durataMin: item.durataMin,
      linguaNome: meta.lingue.find((lang) => lang.id === item.linguaId)?.nome ?? `Lingua ${item.linguaId}`,
      trama: item.trama,
      generiNomi: item.genereIds.map((id) => meta.generi.find((genere) => genere.id === id)?.nome ?? `Genere ${id}`).join(', ')
    }));
  }

  private getMergedFilms(): AdminFilmFormData[] {
    const state = this.sandbox.getFilmState();
    const base = this.readBaseFilmTitles();
    const merged = new Map<number, AdminFilmFormData>();

    for (const film of base) {
      merged.set(film.id, film);
    }
    for (const film of state.created) {
      merged.set(film.id, film);
    }
    for (const [filmId, film] of Object.entries(state.updated)) {
      merged.set(Number(filmId), film);
    }
    for (const deletedId of state.deletedIds) {
      merged.delete(deletedId);
    }

    return Array.from(merged.values()).sort((left, right) => left.id - right.id);
  }

  private readBaseFilmTitles(): AdminFilmFormData[] {
    const titles = this.sandbox.readCacheBody<AdminFilmTitleOption[]>('GET', '/api/admin/film/titoli');
    const publicFilms = this.sandbox.readCacheBody<PublicFilm[]>('GET', '/api/film');
    const meta = this.sandbox.readCacheBody<AdminFilmMetaResponse>('GET', '/api/admin/film/meta');
    if (!titles || !publicFilms) {
      return [];
    }

    const limit = Math.min(titles.length, publicFilms.length);
    return titles.slice(0, limit).map((item, index) => ({
      id: item.id,
      titolo: item.titolo,
      durataMin: publicFilms[index]?.durataMin ?? 0,
      linguaId: meta?.lingue.find((lang) => lang.nome === publicFilms[index]?.linguaNome)?.id ?? index + 1,
      trama: publicFilms[index]?.trama ?? '',
      genereIds: (publicFilms[index]?.generiNomi ?? '')
        .split(',')
        .map((part) => part.trim())
        .filter((part) => !!part)
        .map((name) => meta?.generi.find((genere) => genere.nome === name)?.id)
        .filter((id): id is number => typeof id === 'number')
    }));
  }

  private buildLanguageName(film: AdminFilmFormData): string {
    const cached = this.sandbox.readCacheBody<AdminFilmMetaResponse>('GET', '/api/admin/film/meta');
    return cached?.lingue.find((item) => item.id === film.linguaId)?.nome ?? `Lingua ${film.linguaId}`;
  }

  private buildGenreName(genereId: number): string {
    const cached = this.sandbox.readCacheBody<AdminFilmMetaResponse>('GET', '/api/admin/film/meta');
    return cached?.generi.find((item) => item.id === genereId)?.nome ?? `Genere ${genereId}`;
  }

  private sandboxCall<T>(factory: () => T): Observable<T> {
    try {
      return of(factory());
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Operazione non riuscita.';
      return throwError(() => new HttpErrorResponse({
        status: 400,
        statusText: 'Bad Request',
        error: { message }
      }));
    }
  }
}
