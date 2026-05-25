import { HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthTokenService } from '../service/auth-token.service';

export const authTokenInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
  if (!isApiCall(req.url) || isPublicAuthEndpoint(req.url)) {
    return next(req);
  }

  const tokenService = inject(AuthTokenService);
  const token = tokenService.getToken();
  if (!token) {
    return next(req);
  }

  return next(req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  }));
};

function isApiCall(url: string): boolean {
  return url.includes('/api/');
}

function isPublicAuthEndpoint(url: string): boolean {
  return url.includes('/api/auth/login') || url.includes('/api/auth/register');
}
