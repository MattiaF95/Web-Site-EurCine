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

  getOrdine(numeroOrdine: string): Observable<Ordine> {
    return this.http.get<Ordine>(`${this.apiUrl}/codice/${encodeURIComponent(numeroOrdine)}`);
  }

  getOrdini(): Observable<Ordine[]> {
    return this.http.get<Ordine[]>(this.apiUrl);
  }

  getBiglietti(numeroOrdine: string): Observable<Biglietto[]> {
    return this.http.get<Biglietto[]>(`${this.apiUrl}/codice/${encodeURIComponent(numeroOrdine)}/biglietti`);
  }
}
