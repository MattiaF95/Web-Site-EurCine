import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class AuthTokenService {
  private static readonly TOKEN_KEY = 'eurcine_auth_token';

  private readonly platformId = inject(PLATFORM_ID);
  private token: string | null = null;

  getToken(): string | null {
    if (this.token) {
      return this.token;
    }

    if (!isPlatformBrowser(this.platformId)) {
      return null;
    }

    const storedToken = localStorage.getItem(AuthTokenService.TOKEN_KEY);
    this.token = storedToken && storedToken.trim() ? storedToken : null;
    return this.token;
  }

  setToken(token: string | null): void {
    const normalizedToken = token?.trim() ?? '';
    this.token = normalizedToken ? normalizedToken : null;

    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    if (this.token) {
      localStorage.setItem(AuthTokenService.TOKEN_KEY, this.token);
      return;
    }

    localStorage.removeItem(AuthTokenService.TOKEN_KEY);
  }

  clearToken(): void {
    this.setToken(null);
  }
}
