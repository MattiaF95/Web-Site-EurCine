import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AdminFilmFormData,
  AdminFilmMetaResponse,
  AdminFilmSaveRequest,
  AdminFilmTitleOption
} from '../model/admin-film/admin-film-management.model';

@Injectable({ providedIn: 'root' })
export class FilmManagementService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/api/admin`;

  getFilmTitles(): Observable<AdminFilmTitleOption[]> {
    return this.http.get<AdminFilmTitleOption[]>(`${this.apiUrl}/film/titoli`, { withCredentials: true });
  }

  getMeta(): Observable<AdminFilmMetaResponse> {
    return this.http.get<AdminFilmMetaResponse>(`${this.apiUrl}/film/meta`, { withCredentials: true });
  }

  getFilmById(filmId: number): Observable<AdminFilmFormData> {
    return this.http.get<AdminFilmFormData>(`${this.apiUrl}/film/${filmId}`, { withCredentials: true });
  }

  createFilm(payload: AdminFilmSaveRequest): Observable<AdminFilmFormData> {
    return this.http.post<AdminFilmFormData>(`${this.apiUrl}/film/aggiungi`, payload, { withCredentials: true });
  }

  updateFilm(filmId: number, payload: AdminFilmSaveRequest): Observable<AdminFilmFormData> {
    return this.http.put<AdminFilmFormData>(`${this.apiUrl}/film/modifica/${filmId}`, payload, { withCredentials: true });
  }

  deleteFilm(filmId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/film/elimina/${filmId}`, { withCredentials: true });
  }
}
