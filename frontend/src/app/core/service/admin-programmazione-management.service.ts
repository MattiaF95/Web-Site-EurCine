import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AdminProgrammazioneBatchCreateRequest,
  AdminProgrammazioneBatchCreateResponse,
  AdminProgrammazioneCatalogResponse,
  AdminProgrammazioneCreatedItem
} from '../model/admin-film/admin-film-management.model';
import { SessionSandboxService } from './session-sandbox.service';
import { AuthTokenService } from './auth-token.service';

type PublicProgrammazione = {
  programmazioneId: number;
  filmTitolo: string;
  salaNome: string;
  startAt: string;
  prezzoBasePre18: number;
  prezzoBasePost18: number;
};

@Injectable({ providedIn: 'root' })
export class AdminProgrammazioneManagementService {
  private readonly http = inject(HttpClient);
  private readonly sandbox = inject(SessionSandboxService);
  private readonly authTokenService = inject(AuthTokenService);
  private readonly apiUrl = `${environment.apiBaseUrl}/api/admin/programmazione`;

  getCatalog(): Observable<AdminProgrammazioneCatalogResponse> {
    return this.http.get<AdminProgrammazioneCatalogResponse>(`${this.apiUrl}/catalog`);
  }

  createBatch(payload: AdminProgrammazioneBatchCreateRequest): Observable<AdminProgrammazioneBatchCreateResponse> {
    if (environment.production && this.sandbox.isActive() && this.isMockSession()) {
      return this.sandboxCall(() => this.createMockBatch(payload));
    }

    return this.http.post<AdminProgrammazioneBatchCreateResponse>(`${this.apiUrl}/aggiungi`, payload);
  }

  getByFilm(filmId: number): Observable<AdminProgrammazioneCreatedItem[]> {
    return this.http.get<AdminProgrammazioneCreatedItem[]>(`${this.apiUrl}/film/${filmId}`);
  }

  deleteProgrammazione(programmazioneId: number): Observable<void> {
    if (environment.production && this.sandbox.isActive() && this.isMockSession()) {
      return this.sandboxCall(() => {
        this.deleteMockProgrammazione(programmazioneId);
        return void 0;
      });
    }

    return this.http.delete<void>(`${this.apiUrl}/elimina/${programmazioneId}`);
  }

  private createMockBatch(payload: AdminProgrammazioneBatchCreateRequest): AdminProgrammazioneBatchCreateResponse {
    const catalog = this.sandbox.readCacheBody<AdminProgrammazioneCatalogResponse>('GET', '/api/admin/programmazione/catalog');
    const filmTitle = catalog?.film.find((item) => item.id === payload.filmId)?.titolo ?? `Film ${payload.filmId}`;
    const saleLookup = new Map<number, string>((catalog?.sale ?? []).map((item) => [item.id, item.nome]));
    const state = this.sandbox.getProgrammazioneState();

    const created: AdminProgrammazioneCreatedItem[] = payload.items.map((item, index) => {
      const programmazioneId = state.nextProgrammazioneId + index;
      const startAt = `${payload.giorno}T${item.orario.length === 5 ? `${item.orario}:00` : item.orario}`;
      return {
        programmazioneId,
        filmId: payload.filmId,
        filmTitolo: filmTitle,
        salaId: item.salaId,
        salaNome: saleLookup.get(item.salaId) ?? `Sala ${item.salaId}`,
        startAt,
        prezzoBasePre18: this.resolvePrice(true),
        prezzoBasePost18: this.resolvePrice(false)
      };
    });

    this.sandbox.setProgrammazioneState((draft) => {
      draft.nextProgrammazioneId += created.length;
      draft.created.push(...created);
    });

    this.syncProgrammazioneCaches(created);

    return {
      message: 'Programmazione inserita nella sessione locale.',
      createdCount: created.length,
      created
    };
  }

  private deleteMockProgrammazione(programmazioneId: number): void {
    this.sandbox.setProgrammazioneState((draft) => {
      draft.created = draft.created.filter((item) => item.programmazioneId !== programmazioneId);
      if (!draft.deletedIds.includes(programmazioneId)) {
        draft.deletedIds.push(programmazioneId);
      }
    });

    this.syncProgrammazioneCaches();
  }

  private syncProgrammazioneCaches(createdItems: AdminProgrammazioneCreatedItem[] = []): void {
    const publicList = this.sandbox.readCacheBody<PublicProgrammazione[]>('GET', '/api/programmazione') ?? [];
    const publicDates = this.sandbox.readCacheBody<string[]>('GET', '/api/programmazione/date-disponibili') ?? [];
    const current = this.getMergedProgrammazioni();

    if (publicList.length > 0 || createdItems.length > 0) {
      this.sandbox.setCacheResponse('/api/programmazione', current);
    }

    const mergedDates = new Set<string>(publicDates);
    for (const item of current) {
      mergedDates.add(item.startAt.split('T')[0] ?? item.startAt);
    }
    if (mergedDates.size > 0) {
      this.sandbox.setCacheResponse('/api/programmazione/date-disponibili', Array.from(mergedDates).sort());
    }

    const catalog = this.sandbox.readCacheBody<AdminProgrammazioneCatalogResponse>('GET', '/api/admin/programmazione/catalog');
    if (catalog) {
      this.sandbox.setCacheResponse('/api/admin/programmazione/catalog', {
        ...catalog,
        film: catalog.film
      });
    }

    const createdByFilm = new Map<number, AdminProgrammazioneCreatedItem[]>();
    for (const item of current) {
      const list = createdByFilm.get(item.filmId) ?? [];
      list.push(item);
      createdByFilm.set(item.filmId, list);
    }

    for (const [filmId, items] of createdByFilm.entries()) {
      this.sandbox.setCacheResponse(`/api/admin/programmazione/film/${filmId}`, items);
    }
  }

  private getMergedProgrammazioni(): AdminProgrammazioneCreatedItem[] {
    const state = this.sandbox.getProgrammazioneState();
    const base = this.sandbox.readCacheBody<AdminProgrammazioneCreatedItem[]>('GET', '/api/programmazione') ?? [];
    const merged = [...base, ...state.created];
    return merged.filter((item) => !state.deletedIds.includes(item.programmazioneId));
  }

  private resolvePrice(isPre18: boolean): number {
    const publicList = this.sandbox.readCacheBody<PublicProgrammazione[]>('GET', '/api/programmazione');
    const first = publicList?.[0];
    if (first) {
      return isPre18 ? first.prezzoBasePre18 : first.prezzoBasePost18;
    }
    return isPre18 ? 8.5 : 10;
  }

  private isMockSession(): boolean {
    const token = this.authTokenService.getToken();
    return !!token && token.startsWith('mock-');
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
