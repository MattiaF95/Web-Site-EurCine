import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CreateOrdineRequest, Ordine, Biglietto } from '../model/ordine.model';
import { SessionSandboxService } from './session-sandbox.service';
import { AuthStateService } from './auth-state.service';
import { AuthTokenService } from './auth-token.service';

type SeatMapCache = {
  programmazioneId: number;
  filmTitolo: string;
  salaNome: string;
  startAt: string;
  righe: Array<{ fila: string; posti: Array<{ postoId: number; numero: number; stato: string }> }>;
};

@Injectable({ providedIn: 'root' })
export class OrdineService {
  private readonly http = inject(HttpClient);
  private readonly sandbox = inject(SessionSandboxService);
  private readonly authState = inject(AuthStateService);
  private readonly authTokenService = inject(AuthTokenService);
  private readonly apiUrl = `${environment.apiBaseUrl}/api/ordini`;

  createOrdine(request: CreateOrdineRequest): Observable<Ordine> {
    if (environment.production && this.sandbox.isActive() && this.isMockSession()) {
      try {
        return of(this.createMockOrder(request));
      } catch (error) {
        return throwError(() => this.toHttpError(error, 'Prenotazione non riuscita.'));
      }
    }

    return this.http.post<Ordine>(this.apiUrl, request);
  }

  getOrdine(numeroOrdine: string): Observable<Ordine> {
    if (environment.production && this.sandbox.isActive() && this.isMockSession()) {
      const order = this.sandbox.getMockOrder(numeroOrdine);
      if (order) {
        return of(order);
      }
    }

    return this.http.get<Ordine>(`${this.apiUrl}/codice/${encodeURIComponent(numeroOrdine)}`);
  }

  getOrdini(): Observable<Ordine[]> {
    if (environment.production && this.sandbox.isActive() && this.isMockSession()) {
      return of(this.sandbox.getMockOrders());
    }

    return this.http.get<Ordine[]>(this.apiUrl);
  }

  getBiglietti(numeroOrdine: string): Observable<Biglietto[]> {
    if (environment.production && this.sandbox.isActive() && this.isMockSession()) {
      return of(this.sandbox.getMockBiglietti(numeroOrdine));
    }

    return this.http.get<Biglietto[]>(`${this.apiUrl}/codice/${encodeURIComponent(numeroOrdine)}/biglietti`);
  }

  private createMockOrder(request: CreateOrdineRequest): Ordine {
    const session = this.authState.session();
    const seatMap = this.sandbox.readCacheBody<SeatMapCache>('GET', `/api/programmazione/${request.programmazioneId}/seat-map`);
    if (!seatMap) {
      throw new Error('Seat map non disponibile nella sessione.');
    }

    const selectedSeats = new Set(request.postoIds);
    const seatLookup = new Map<number, { fila: string; numero: number }>();
    for (const row of seatMap.righe) {
      for (const seat of row.posti) {
        if (selectedSeats.has(seat.postoId)) {
          seatLookup.set(seat.postoId, { fila: row.fila, numero: seat.numero });
        }
      }
    }

    const nextOrderNumber = this.sandbox.getNextOrderNumber();
    const numeroOrdine = `ORD-${String(nextOrderNumber).padStart(6, '0')}`;
    const prezzoBiglietto = 10;
    const biglietti: Biglietto[] = request.postoIds.map((postoId, index) => {
      const place = seatLookup.get(postoId);
      return {
        bigliettoId: nextOrderNumber * 100 + index + 1,
        programmazioneId: request.programmazioneId,
        filmTitolo: seatMap.filmTitolo,
        salaNome: seatMap.salaNome,
        startAt: seatMap.startAt,
        fila: place?.fila ?? 'X',
        postoNumero: place?.numero ?? postoId,
        prezzo: prezzoBiglietto
      };
    });

    const ordine: Ordine = {
      numeroOrdine,
      nomeCliente: session ? `${session.nome} ${session.cognome}` : 'Cliente Sessione',
      totale: biglietti.reduce((sum, item) => sum + item.prezzo, 0),
      createdAt: new Date().toISOString(),
      biglietti
    };

    this.sandbox.createMockOrder({
      ordine,
      programmazioneId: request.programmazioneId,
      postoIds: [...request.postoIds]
    });

    this.sandbox.setCacheResponse('/api/ordini', [...this.sandbox.getMockOrders()]);
    this.sandbox.setCacheResponse(`/api/ordini/codice/${encodeURIComponent(numeroOrdine)}`, ordine);
    this.sandbox.setCacheResponse(`/api/ordini/codice/${encodeURIComponent(numeroOrdine)}/biglietti`, biglietti);
    this.patchSeatMapCache(request.programmazioneId, request.postoIds);

    return ordine;
  }

  private patchSeatMapCache(programmazioneId: number, postoIds: number[]): void {
    const seatMap = this.sandbox.readCacheBody<SeatMapCache>('GET', `/api/programmazione/${programmazioneId}/seat-map`);
    if (!seatMap) {
      return;
    }

    const selected = new Set(postoIds);
    const patched: SeatMapCache = {
      ...seatMap,
      righe: seatMap.righe.map((row) => ({
        ...row,
        posti: row.posti.map((seat) => (selected.has(seat.postoId) ? { ...seat, stato: 'OCCUPIED' } : seat))
      }))
    };

    this.sandbox.setCacheResponse(`/api/programmazione/${programmazioneId}/seat-map`, patched);
  }

  private isMockSession(): boolean {
    const token = this.authTokenService.getToken();
    return !!token && token.startsWith('mock-');
  }

  private toHttpError(error: unknown, fallbackMessage: string): HttpErrorResponse {
    const message = error instanceof Error ? error.message : fallbackMessage;
    return new HttpErrorResponse({
      status: 400,
      statusText: 'Bad Request',
      error: { message }
    });
  }
}
