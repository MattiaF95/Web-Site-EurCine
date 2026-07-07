import { Injectable, inject } from '@angular/core';
import { AuthMeResponse, LoginResponse, RegisterRequest } from '../model/auth.model';
import { AdminFilmFormData, AdminProgrammazioneCreatedItem } from '../model/admin-film/admin-film-management.model';
import { CreateOrdineRequest, Ordine, Biglietto } from '../model/ordine.model';
import { AuthTokenService } from './auth-token.service';
import { SessionStorageService } from './session-storage.service';

type CacheEntry<T> = {
  timestamp: number;
  body: T;
  status: number;
  statusText: string;
};

type MockUser = AuthMeResponse & {
  password: string;
};

type MockOrderRecord = {
  ordine: Ordine;
  programmazioneId: number;
  postoIds: number[];
};

type SandboxDirectory = {
  users: MockUser[];
  nextUserId: number;
};

type SandboxWorkspace = {
  auth: {
    activeUserId: number | null;
    activeToken: string | null;
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

const DIRECTORY_KEY = 'eurcine_session_sandbox_directory_v1';
const WORKSPACE_PREFIX = 'eurcine_session_sandbox_workspace_v1:';
const CACHE_PREFIX = 'eurcine_http_cache:';

const INITIAL_DIRECTORY: SandboxDirectory = {
  users: [
    {
      utenteId: 1,
      nome: 'Admin',
      cognome: 'Eurcine',
      email: 'admin@eurcine.it',
      ruolo: 'SUPER_ADMIN',
      message: 'Sessione mock pronta.',
      password: 'admin123'
    }
  ],
  nextUserId: 2
};

const INITIAL_WORKSPACE: SandboxWorkspace = {
  auth: {
    activeUserId: null,
    activeToken: null
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
  private readonly authTokenService = inject(AuthTokenService);

  isActive(): boolean {
    return this.storage.isBrowser();
  }

  getDirectory(): SandboxDirectory {
    return this.storage.readJson(DIRECTORY_KEY, this.cloneInitialDirectory());
  }

  updateDirectory(mutator: (draft: SandboxDirectory) => void): SandboxDirectory {
    const draft = this.getDirectory();
    mutator(draft);
    this.storage.writeJson(DIRECTORY_KEY, draft);
    return draft;
  }

  getMockSession(token?: string | null): AuthMeResponse | null {
    const workspace = this.getWorkspace(token);
    if (!workspace.auth.activeUserId) {
      return null;
    }

    const user = this.getDirectory().users.find((item) => item.utenteId === workspace.auth.activeUserId) ?? null;
    return user ? this.toMeResponse(user) : null;
  }

  loginMock(payload: { email: string; password: string }): LoginResponse {
    const normalizedEmail = payload.email.trim().toLowerCase();
    const normalizedPassword = payload.password;
    const user = this.getDirectory().users.find(
      (item) => item.email.toLowerCase() === normalizedEmail && item.password === normalizedPassword
    );

    if (!user) {
      throw new Error('Credenziali non valide.');
    }

    const token = this.generateToken(user.utenteId);
    const workspace: SandboxWorkspace = {
      ...this.cloneInitialWorkspace(),
      auth: {
        activeUserId: user.utenteId,
        activeToken: token
      }
    };

    this.writeWorkspace(token, workspace);
    this.setCacheResponse('/api/auth/me', this.toMeResponse(user), 'GET', 200, 'OK', token);
    return this.toLoginResponse(user, token, 'Login effettuato con successo.');
  }

  registerMock(payload: RegisterRequest): LoginResponse {
    const normalizedEmail = payload.email.trim().toLowerCase();
    let createdUser: MockUser | undefined;

    this.updateDirectory((draft) => {
      if (draft.users.some((item) => item.email.toLowerCase() === normalizedEmail)) {
        throw new Error('Esiste già un account con questa email.');
      }

      createdUser = {
        utenteId: draft.nextUserId,
        nome: payload.nome.trim(),
        cognome: payload.cognome.trim(),
        email: payload.email.trim(),
        ruolo: 'USER',
        message: 'Registrazione effettuata con successo.',
        password: payload.password
      };

      draft.nextUserId += 1;
      draft.users.push(createdUser);
    });

    if (!createdUser) {
      throw new Error('Registrazione non riuscita.');
    }

    const user = createdUser;
    const token = this.generateToken(user.utenteId);
    const workspace: SandboxWorkspace = {
      ...this.cloneInitialWorkspace(),
      auth: {
        activeUserId: user.utenteId,
        activeToken: token
      }
    };

    this.writeWorkspace(token, workspace);
    this.setCacheResponse('/api/auth/me', this.toMeResponse(user), 'GET', 200, 'OK', token);
    return this.toLoginResponse(user, token, 'Registrazione effettuata con successo.');
  }

  logoutMock(): void {
    // The active token is cleared by AuthStateService; workspaces remain isolated per session token.
  }

  createMockOrder(record: MockOrderRecord): void {
    this.updateWorkspace((draft) => {
      draft.orders.items.push(record);
      draft.orders.nextOrderNumber += 1;
    });
  }

  getMockOrders(): Ordine[] {
    return this.getWorkspace().orders.items.map((item) => item.ordine);
  }

  getMockOrder(numeroOrdine: string): Ordine | null {
    return this.getWorkspace().orders.items.find((item) => item.ordine.numeroOrdine === numeroOrdine)?.ordine ?? null;
  }

  getMockBiglietti(numeroOrdine: string): Biglietto[] {
    return this.getMockOrder(numeroOrdine)?.biglietti ?? [];
  }

  getNextOrderNumber(): number {
    return this.getWorkspace().orders.nextOrderNumber;
  }

  getFilmState(): SandboxWorkspace['films'] {
    return this.getWorkspace().films;
  }

  setFilmState(updater: (draft: SandboxWorkspace['films']) => void): void {
    this.updateWorkspace((draft) => updater(draft.films));
  }

  getProgrammazioneState(): SandboxWorkspace['programmazioni'] {
    return this.getWorkspace().programmazioni;
  }

  setProgrammazioneState(updater: (draft: SandboxWorkspace['programmazioni']) => void): void {
    this.updateWorkspace((draft) => updater(draft.programmazioni));
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

  setCacheResponse<T>(
    urlWithParams: string,
    body: T,
    method = 'GET',
    status = 200,
    statusText = 'OK',
    token?: string | null
  ): void {
    this.storage.writeJson<CacheEntry<T>>(this.cacheKey(method, urlWithParams, token), {
      timestamp: Date.now(),
      body,
      status,
      statusText
    });
  }

  removeCache(urlWithParams: string, method = 'GET', token?: string | null): void {
    this.storage.removeItem(this.cacheKey(method, urlWithParams, token));
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

  private getWorkspace(token?: string | null): SandboxWorkspace {
    return this.storage.readJson(this.workspaceKey(token), this.cloneInitialWorkspace());
  }

  private updateWorkspace(mutator: (draft: SandboxWorkspace) => void, token?: string | null): SandboxWorkspace {
    const draft = this.getWorkspace(token);
    mutator(draft);
    this.storage.writeJson(this.workspaceKey(token), draft);
    return draft;
  }

  private writeWorkspace(token: string, workspace: SandboxWorkspace): void {
    this.storage.writeJson(this.workspaceKey(token), workspace);
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

  private resolveScopeToken(token?: string | null): string {
    const normalized = token?.trim() ?? this.authTokenService.getToken()?.trim() ?? '';
    return normalized || 'anon';
  }

  private workspaceKey(token?: string | null): string {
    return `${WORKSPACE_PREFIX}${this.resolveScopeToken(token)}`;
  }

  private cacheKey(method: string, urlWithParams: string, token?: string | null): string {
    return `${CACHE_PREFIX}${this.resolveScopeToken(token)}:${method}:${urlWithParams}`;
  }

  private cloneInitialDirectory(): SandboxDirectory {
    return JSON.parse(JSON.stringify(INITIAL_DIRECTORY)) as SandboxDirectory;
  }

  private cloneInitialWorkspace(): SandboxWorkspace {
    return JSON.parse(JSON.stringify(INITIAL_WORKSPACE)) as SandboxWorkspace;
  }
}
