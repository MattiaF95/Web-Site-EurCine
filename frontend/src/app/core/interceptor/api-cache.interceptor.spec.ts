import '@angular/compiler';
import 'zone.js';
import 'zone.js/testing';
import { HttpRequest, HttpResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { firstValueFrom, of } from 'rxjs';
import { beforeEach, describe, expect, it } from 'vitest';
import { apiCacheInterceptor } from './api-cache.interceptor';
import { AuthTokenService } from '../service/auth-token.service';
import { SessionStorageService } from '../service/session-storage.service';

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

describe('apiCacheInterceptor', () => {
  const storage = createMemoryStorage();
  const tokenService = createTokenService();

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: SessionStorageService, useValue: storage },
        { provide: AuthTokenService, useValue: tokenService }
      ]
    });
  });

  it('keeps GET cache isolated per session token', async () => {
    const request = new HttpRequest('GET', '/api/film');
    let calls = 0;

    tokenService.setToken('mock-session-a');
    const first = await firstValueFrom(
      TestBed.runInInjectionContext(() =>
        apiCacheInterceptor(request, () => {
          calls += 1;
          return of(new HttpResponse({
            body: ['A'],
            status: 200,
            statusText: 'OK',
            url: request.urlWithParams
          }));
        }))
    );
    expect((first as HttpResponse<unknown>).body).toEqual(['A']);
    expect(calls).toBe(1);

    tokenService.setToken('mock-session-b');
    const second = await firstValueFrom(
      TestBed.runInInjectionContext(() =>
        apiCacheInterceptor(request, () => {
          calls += 1;
          return of(new HttpResponse({
            body: ['B'],
            status: 200,
            statusText: 'OK',
            url: request.urlWithParams
          }));
        }))
    );
    expect((second as HttpResponse<unknown>).body).toEqual(['B']);
    expect(calls).toBe(2);

    tokenService.setToken('mock-session-b');
    const third = await firstValueFrom(
      TestBed.runInInjectionContext(() =>
        apiCacheInterceptor(request, () => {
          calls += 1;
          return of(new HttpResponse({
            body: ['IGNORED'],
            status: 200,
            statusText: 'OK',
            url: request.urlWithParams
          }));
        }))
    );
    expect((third as HttpResponse<unknown>).body).toEqual(['B']);
    expect(calls).toBe(2);
  });
});
