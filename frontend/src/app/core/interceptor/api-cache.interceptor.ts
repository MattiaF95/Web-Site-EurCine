import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';

type CacheEntry = {
  timestamp: number;
  body: unknown;
  status: number;
  statusText: string;
};

const CACHE_PREFIX = 'eurcine_http_cache:';
const TTL_BY_PATH: Array<{ path: string; ttlMs: number }> = [
  { path: '/api/film', ttlMs: 15 * 60 * 1000 },
  { path: '/api/sale', ttlMs: 15 * 60 * 1000 },
  { path: '/api/programmazione', ttlMs: 3 * 60 * 1000 },
  { path: '/api/home', ttlMs: 2 * 60 * 1000 },
  { path: '/api/admin/film/titoli', ttlMs: 2 * 60 * 1000 },
  { path: '/api/admin/film/meta', ttlMs: 5 * 60 * 1000 }
];

const INVALIDATE_PREFIXES = [
  '/api/admin/film',
  '/api/programmazione',
  '/api/ordini'
];

export const apiCacheInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
  if (!isBrowser()) {
    return next(req);
  }

  if (req.method === 'GET') {
    const ttlMs = getTtlMs(req.url);
    if (ttlMs > 0) {
      const cacheKey = getCacheKey(req);
      const cached = readCache(cacheKey, ttlMs);
      if (cached) {
        return of(new HttpResponse({
          body: cached.body,
          status: cached.status,
          statusText: cached.statusText,
          url: req.urlWithParams
        }));
      }

      return next(req).pipe(
        tap((event) => {
          if (event instanceof HttpResponse) {
            writeCache(cacheKey, {
              timestamp: Date.now(),
              body: event.body,
              status: event.status,
              statusText: event.statusText
            });
          }
        })
      );
    }
    return next(req);
  }

  if (req.method === 'POST' || req.method === 'PUT' || req.method === 'PATCH' || req.method === 'DELETE') {
    return next(req).pipe(
      tap((event) => {
        if (event instanceof HttpResponse && shouldInvalidate(req.url)) {
          invalidateCaches();
        }
      })
    );
  }

  return next(req);
};

function getCacheKey(req: HttpRequest<unknown>): string {
  return `${CACHE_PREFIX}${req.method}:${req.urlWithParams}`;
}

function getTtlMs(url: string): number {
  const match = TTL_BY_PATH.find((entry) => url.includes(entry.path));
  return match ? match.ttlMs : 0;
}

function shouldInvalidate(url: string): boolean {
  return INVALIDATE_PREFIXES.some((prefix) => url.includes(prefix));
}

function readCache(key: string, ttlMs: number): CacheEntry | null {
  try {
    const raw = localStorage.getItem(key);
    if (!raw) {
      return null;
    }
    const parsed = JSON.parse(raw) as CacheEntry;
    if (Date.now() - parsed.timestamp > ttlMs) {
      localStorage.removeItem(key);
      return null;
    }
    return parsed;
  } catch {
    return null;
  }
}

function writeCache(key: string, value: CacheEntry): void {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch {
    // Ignore quota/storage errors and continue without caching.
  }
}

function invalidateCaches(): void {
  try {
    const keys: string[] = [];
    for (let i = 0; i < localStorage.length; i += 1) {
      const key = localStorage.key(i);
      if (key && key.startsWith(CACHE_PREFIX)) {
        keys.push(key);
      }
    }
    keys.forEach((key) => localStorage.removeItem(key));
  } catch {
    // Ignore storage errors.
  }
}

function isBrowser(): boolean {
  return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
}
