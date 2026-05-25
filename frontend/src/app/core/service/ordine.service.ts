import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CreateOrdineRequest, Ordine, Biglietto } from '../model/ordine.model';

@Injectable({ providedIn: 'root' })
export class OrdineService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/api/ordini`;

  createOrdine(request: CreateOrdineRequest): Observable<Ordine> {
    return this.http.post<Ordine>(this.apiUrl, request);
  }

  getOrdine(ordineId: number): Observable<Ordine> {
    return this.http.get<Ordine>(`${this.apiUrl}/${ordineId}`);
  }

  getOrdini(): Observable<Ordine[]> {
    return this.http.get<Ordine[]>(this.apiUrl);
  }

  getBiglietti(ordineId: number): Observable<Biglietto[]> {
    return this.http.get<Biglietto[]>(`${this.apiUrl}/${ordineId}/biglietti`);
  }
}
