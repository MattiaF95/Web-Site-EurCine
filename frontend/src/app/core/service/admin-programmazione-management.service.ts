import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AdminProgrammazioneBatchCreateRequest,
  AdminProgrammazioneBatchCreateResponse,
  AdminProgrammazioneCatalogResponse,
  AdminProgrammazioneCreatedItem
} from '../model/admin-film/admin-film-management.model';

@Injectable({ providedIn: 'root' })
export class AdminProgrammazioneManagementService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/api/admin/programmazione`;

  getCatalog(): Observable<AdminProgrammazioneCatalogResponse> {
    return this.http.get<AdminProgrammazioneCatalogResponse>(`${this.apiUrl}/catalog`, { withCredentials: true });
  }

  createBatch(payload: AdminProgrammazioneBatchCreateRequest): Observable<AdminProgrammazioneBatchCreateResponse> {
    return this.http.post<AdminProgrammazioneBatchCreateResponse>(`${this.apiUrl}/aggiungi`, payload, {
      withCredentials: true
    });
  }

  getByFilm(filmId: number): Observable<AdminProgrammazioneCreatedItem[]> {
    return this.http.get<AdminProgrammazioneCreatedItem[]>(`${this.apiUrl}/film/${filmId}`, {
      withCredentials: true
    });
  }

  deleteProgrammazione(programmazioneId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/elimina/${programmazioneId}`, {
      withCredentials: true
    });
  }
}
