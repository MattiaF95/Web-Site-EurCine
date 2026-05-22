import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Programmazione } from '../model/programmazione.model';
import { environment } from '../../../environments/environment';
import { SeatMap } from '../model/seat-map.model';

@Injectable({ providedIn: 'root' })
export class ProgrammazioneService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/api/programmazione`;

  getAll(): Observable<Programmazione[]> {
    return this.http.get<Programmazione[]>(this.apiUrl);
  }

  getAvailableDates(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/date-disponibili`);
  }

  getSeatMap(programmazioneId: number): Observable<SeatMap> {
    return this.http.get<SeatMap>(`${this.apiUrl}/${programmazioneId}/seat-map`);
  }
}
