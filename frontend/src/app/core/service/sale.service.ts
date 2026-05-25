import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sala } from '../model/sala.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SaleService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/api/sale`;

  getAll(): Observable<Sala[]> {
    return this.http.get<Sala[]>(this.apiUrl);
  }

  getOne(nome: string): Observable<Sala> {
    return this.http.get<Sala>(`${this.apiUrl}/${encodeURIComponent(nome)}`);
  }
}
