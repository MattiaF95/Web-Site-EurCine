import '@angular/compiler';
import 'zone.js';
import 'zone.js/testing';
import { TestBed } from '@angular/core/testing';
import { beforeEach, describe, expect, it } from 'vitest';
import { SessionSandboxService } from './session-sandbox.service';
import { SessionStorageService } from './session-storage.service';
import { AuthTokenService } from './auth-token.service';
import { AdminFilmFormData } from '../model/admin-film/admin-film-management.model';

function createMemoryStorage(): SessionStorageService {
  const store = new Map<string, string>();

  return {
    isBrowser: () => true,
    getItem: (key: string) => store.get(key) ?? null,
    setItem: (key: string, value: string) => {
      store.set(key, value);
    },
    removeItem: (key: string) => {
      store.delete(key);
    },
    keys: () => Array.from(store.keys()),
    readJson: <T>(key: string, fallback: T) => {
      const raw = store.get(key);
      if (!raw) {
        return fallback;
      }

      try {
        return JSON.parse(raw) as T;
      } catch {
        return fallback;
      }
    },
    writeJson: <T>(key: string, value: T) => {
      store.set(key, JSON.stringify(value));
    },
    updateJson: <T>(key: string, fallback: T, mutator: (draft: T) => void) => {
      const raw = store.get(key);
      const draft = raw ? (JSON.parse(raw) as T) : fallback;
      mutator(draft);
      store.set(key, JSON.stringify(draft));
      return draft;
    }
  } as SessionStorageService;
}

function createTokenService(): AuthTokenService {
  let token: string | null = null;

  return {
    getToken: () => token,
    setToken: (next: string | null) => {
      token = next;
    },
    clearToken: () => {
      token = null;
    }
  } as AuthTokenService;
}

describe('SessionSandboxService', () => {
  const storage = createMemoryStorage();
  const tokenService = createTokenService();

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        SessionSandboxService,
        { provide: SessionStorageService, useValue: storage },
        { provide: AuthTokenService, useValue: tokenService }
      ]
    });
  });

  it('keeps user workspace isolated by session token and resets on new login', () => {
    const sandbox = TestBed.inject(SessionSandboxService);

    const registration = sandbox.registerMock({
      nome: 'Luca',
      cognome: 'Rossi',
      email: 'luca.rossi@example.com',
      password: 'secret123'
    });
    tokenService.setToken(registration.token);

    const film: AdminFilmFormData = {
      id: 1000,
      titolo: 'Film sessione',
      durataMin: 120,
      linguaId: 1,
      trama: 'Trama',
      genereIds: [1]
    };

    sandbox.setFilmState((draft) => {
      draft.created.push(film);
    });
    expect(sandbox.getFilmState().created).toHaveLength(1);

    tokenService.clearToken();
    const login = sandbox.loginMock({
      email: 'luca.rossi@example.com',
      password: 'secret123'
    });
    tokenService.setToken(login.token);

    expect(sandbox.getFilmState().created).toHaveLength(0);
    expect(sandbox.getMockSession()).toMatchObject({
      email: 'luca.rossi@example.com',
      nome: 'Luca'
    });
  });
});
