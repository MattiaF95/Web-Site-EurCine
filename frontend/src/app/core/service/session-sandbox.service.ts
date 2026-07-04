import { Injectable, inject } from '@angular/core';
import { AuthMeResponse, LoginResponse, RegisterRequest } from '../model/auth.model';
import { AdminFilmFormData, AdminProgrammazioneCreatedItem } from '../model/admin-film/admin-film-management.model';
import { CreateOrdineRequest, Ordine, Biglietto } from '../model/ordine.model';
import { SessionStorageService } from './session-storage.service';

type CacheEntry<T> = {
  timestamp: number;
  body: T;
  status: number;
  statusText: string;
};

type MockUser = AuthMeResponse & {
  password: string;
  token: string;
};

type MockOrderRecord = {
  ordine: Ordine;
  programmazioneId: number;
  postoIds: number[];
};

type SandboxState = {
  auth: {
    users: MockUser[];
    activeUserId: number | null;
    activeToken: string | null;
    nextUserId: number;
  };
  orders: {
    items: MockOrderRecord[];
    nextOrderNumber: number;
  };
  films: {
    created: AdminFilmFormData[];
    updated: Record<number, AdminFilmFormData>;
    deletedIds: number[];
    nextFilmId: number;
  };
  programmazioni: {
    created: AdminProgrammazioneCreatedItem[];
    deletedIds: number[];
    nextProgrammazioneId: number;
  };
};

const STATE_KEY = 'eurcine_session_sandbox_state_v1';
const CACHE_PREFIX = 'eurcine_http_cache:';

const INITIAL_STATE: SandboxState = {
  auth: {
    users: [
      {
        utenteId: 1,
        nome: 'Admin',
        cognome: 'Eurcine',
        email: 'admin@eurcine.it',
        ruolo: 'SUPER_ADMIN',
        message: 'Sessione mock pronta.',
        password: 'admin123',
        token: ''
      }
    ],
    activeUserId: null,
    activeToken: null,
    nextUserId: 2
  },
  orders: {
    items: [],
    nextOrderNumber: 1
  },
  films: {
    created: [],
    updated: {},
    deletedIds: [],
    nextFilmId: 1000
  },
  programmazioni: {
    created: [],
    deletedIds: [],
    nextProgrammazioneId: 100000
  }
};

@Injectable({ providedIn: 'root' })
export class SessionSandboxService {
  private readonly storage = inject(SessionStorageService);

  isActive(): boolean {
    return this.storage.isBrowser();
  }

  getState(): SandboxState {
    return this.storage.readJson(STATE_KEY, this.cloneInitialState());
  }

  updateState(mutator: (draft: SandboxState) => void): SandboxState {
    const draft = this.getState();
    mutator(draft);
    this.storage.writeJson(STATE_KEY, draft);
    return draft;
  }

  getMockSession(token?: string | null): AuthMeResponse | null {
    const state = this.getState();
    const current = state.auth.users.find((user) => user.utenteId === state.auth.activeUserId) ?? null;
    if (!current) {
      return null;
    }
    if (token && state.auth.activeToken && token !== state.auth.activeToken) {
      return null;
    }
    return this.toMeResponse(current);
  }

  loginMock(payload: { email: string; password: string }): LoginResponse {
    const normalizedEmail = payload.email.trim().toLowerCase();
    const normalizedPassword = payload.password;
    let response: LoginResponse | null = null;

    this.updateState((draft) => {
      const user = draft.auth.users.find(
        (item) => item.email.toLowerCase() === normalizedEmail && item.password === normalizedPassword
      );
      if (!user) {
        throw new Error('Credenziali non valide.');
      }

      draft.auth.activeUserId = user.utenteId;
      draft.auth.activeToken = this.generateToken(user.utenteId);
      user.token = draft.auth.activeToken;
      response = this.toLoginResponse(user, draft.auth.activeToken, 'Login effettuato con successo.');
    });

    if (!response) {
      throw new Error('Login non riuscito.');
    }

    this.setCacheResponse('/api/auth/me', this.getMockSession());
    return response;
  }

  registerMock(payload: RegisterRequest): LoginResponse {
    let response: LoginResponse | null = null;
    const normalizedEmail = payload.email.trim().toLowerCase();

    this.updateState((draft) => {
      if (draft.auth.users.some((item) => item.email.toLowerCase() === normalizedEmail)) {
        throw new Error('Esiste già un account con questa email.');
      }

      const user: MockUser = {
        utenteId: draft.auth.nextUserId,
        nome: payload.nome.trim(),
        cognome: payload.cognome.trim(),
        email: payload.email.trim(),
        ruolo: 'USER',
        message: 'Registrazione effettuata con successo.',
        password: payload.password,
        token: ''
      };

      draft.auth.nextUserId += 1;
      draft.auth.users.push(user);
      draft.auth.activeUserId = user.utenteId;
      draft.auth.activeToken = this.generateToken(user.utenteId);
      user.token = draft.auth.activeToken;
      response = this.toLoginResponse(user, draft.auth.activeToken, 'Registrazione effettuata con successo.');
    });

    if (!response) {
      throw new Error('Registrazione non riuscita.');
    }

    this.setCacheResponse('/api/auth/me', this.getMockSession());
    return response;
  }

  logoutMock(): void {
    this.updateState((draft) => {
      draft.auth.activeUserId = null;
      draft.auth.activeToken = null;
    });
  }

  createMockOrder(record: MockOrderRecord): void {
    this.updateState((draft) => {
      draft.orders.items.push(record);
      draft.orders.nextOrderNumber += 1;
    });
  }

  getMockOrders(): Ordine[] {
    return this.getState().orders.items.map((item) => item.ordine);
  }

  getMockOrder(numeroOrdine: string): Ordine | null {
    return this.getState().orders.items.find((item) => item.ordine.numeroOrdine === numeroOrdine)?.ordine ?? null;
  }

  getMockBiglietti(numeroOrdine: string): Biglietto[] {
    return this.getMockOrder(numeroOrdine)?.biglietti ?? [];
  }

  getNextOrderNumber(): number {
    return this.getState().orders.nextOrderNumber;
  }

  getFilmState(): SandboxState['films'] {
    return this.getState().films;
  }

  setFilmState(updater: (draft: SandboxState['films']) => void): void {
    this.updateState((draft) => updater(draft.films));
  }

  getProgrammazioneState(): SandboxState['programmazioni'] {
    return this.getState().programmazioni;
  }

  setProgrammazioneState(updater: (draft: SandboxState['programmazioni']) => void): void {
    this.updateState((draft) => updater(draft.programmazioni));
  }

  readCache<T>(method: string, urlWithParams: string): CacheEntry<T> | null {
    const raw = this.storage.getItem(this.cacheKey(method, urlWithParams));
    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as CacheEntry<T>;
    } catch {
      return null;
    }
  }

  readCacheBody<T>(method: string, urlWithParams: string): T | null {
    return this.readCache<T>(method, urlWithParams)?.body ?? null;
  }

  setCacheResponse<T>(urlWithParams: string, body: T, method = 'GET', status = 200, statusText = 'OK'): void {
    this.storage.writeJson<CacheEntry<T>>(this.cacheKey(method, urlWithParams), {
      timestamp: Date.now(),
      body,
      status,
      statusText
    });
  }

  removeCache(urlWithParams: string, method = 'GET'): void {
    this.storage.removeItem(this.cacheKey(method, urlWithParams));
  }

  removeCachesContaining(fragment: string): void {
    for (const key of this.storage.keys()) {
      if (key.startsWith(CACHE_PREFIX) && key.includes(fragment)) {
        this.storage.removeItem(key);
      }
    }
  }

  private generateToken(userId: number): string {
    const nonce = typeof crypto !== 'undefined' && typeof crypto.randomUUID === 'function'
      ? crypto.randomUUID()
      : `${Date.now()}-${Math.random().toString(16).slice(2)}`;
    return `mock-${userId}-${nonce}`;
  }

  private toMeResponse(user: MockUser): AuthMeResponse {
    return {
      utenteId: user.utenteId,
      nome: user.nome,
      cognome: user.cognome,
      email: user.email,
      ruolo: user.ruolo,
      message: user.message
    };
  }

  private toLoginResponse(user: MockUser, token: string, message: string): LoginResponse {
    return {
      utenteId: user.utenteId,
      nome: user.nome,
      cognome: user.cognome,
      email: user.email,
      ruolo: user.ruolo,
      message,
      token
    };
  }

  private cacheKey(method: string, urlWithParams: string): string {
    return `${CACHE_PREFIX}${method}:${urlWithParams}`;
  }

  private cloneInitialState(): SandboxState {
    return JSON.parse(JSON.stringify(INITIAL_STATE)) as SandboxState;
  }
}
