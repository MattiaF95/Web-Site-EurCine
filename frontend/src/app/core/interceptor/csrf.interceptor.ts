import { HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';

const CSRF_COOKIE_NAME = 'XSRF-TOKEN';
const CSRF_HEADER_NAME = 'X-XSRF-TOKEN';
const MUTATING_METHODS = new Set(['POST', 'PUT', 'PATCH', 'DELETE']);

export const csrfInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
  if (!isBrowser()) {
    return next(req);
  }

  if (!isApiCall(req.url)) {
    return next(req);
  }

  let nextReq = req;
  if (!req.withCredentials) {
    nextReq = nextReq.clone({ withCredentials: true });
  }

  if (!MUTATING_METHODS.has(req.method)) {
    return next(nextReq);
  }

  const csrfToken = readCookie(CSRF_COOKIE_NAME);
  if (!csrfToken) {
    return next(nextReq);
  }

  return next(nextReq.clone({
    setHeaders: {
      [CSRF_HEADER_NAME]: csrfToken
    }
  }));
};

function isBrowser(): boolean {
  return typeof document !== 'undefined';
}

function isApiCall(url: string): boolean {
  return url.includes('/api/');
}

function readCookie(name: string): string | null {
  const rawCookie = document.cookie
    .split('; ')
    .find((entry) => entry.startsWith(`${name}=`));

  if (!rawCookie) {
    return null;
  }

  const encodedValue = rawCookie.substring(name.length + 1);
  return decodeURIComponent(encodedValue);
}
