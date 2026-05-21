import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Film } from '../model/film.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class FilmService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/api/film`;

  getAll(): Observable<Film[]> {
    return this.http.get<Film[]>(this.apiUrl);
  }

  getOne(titolo: string): Observable<Film> {
    return this.http.get<Film>(`${this.apiUrl}/${encodeURIComponent(titolo)}`);
  }
}
